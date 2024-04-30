package paf.practice.repo;

import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.MongoExpression;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationExpression;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.BucketOperation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.aggregation.StringOperators;
import org.springframework.data.mongodb.core.aggregation.UnwindOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.stereotype.Repository;

import paf.practice.model.Game;

@Repository
public class GameRepo {
    
    @Autowired
    private MongoTemplate template;

    // CRUD
    // - insert = if got _id that already exists in DB --> throw error
    // - save = if got _id that already exists in DB --> updates record
    public Game createGame(Game game) {
        return template.insert(game);
        // return template.save(game);
    }

    public Game retrieveGameByGameId(int gameId) {
        Query query = new Query(Criteria.where("gid").is(gameId));
        return template.findOne(query, Game.class);
    }

    public Game retrieveGameByObjectId(ObjectId objectId) {
        Query query = new Query(Criteria.where("_id").is(objectId));
        return template.findOne(query, Game.class);
    }

    public List<Game> retrieveAllGames(Game game) {
        return template.findAll(Game.class);
    }

    public List<Game> retrieveGamesByName(String searchString) {
        Query query = new Query(Criteria.where("name").regex(searchString,"i"));
        return template.find(query, Game.class);
    }

    public List<Game> retrieveGamesByNameUsingIndex(String searchString) {
        TextCriteria textCriteria = TextCriteria.forDefaultLanguage().matchingPhrase(searchString);
        TextQuery textQuery = TextQuery.queryText(textCriteria);
        List<Game> results = template.find(textQuery, Game.class, "games");
        return results;
    }

    public void updateGame(Game game) {
        
    }

    // public void updateGames(ObjectId objId, Person person) {
    //     Query query = Query.query(Criteria.where("_id").is(objId));
    //     Update updateOperation = new Update()
    //         .set("name", person.getName())
    //         .inc("age", 1);
    //     UpdateResult result = template.updateMulti(query, updateOperation, "persons");
    //     System.out.println("Document updated: " + result.getModifiedCount());
    // }

    public void deleteGame(Game game) {
        
    }

    public Long countGamesByYear(int year) {
        Criteria criteria = Criteria
                .where("year").is(year);
        Query query = Query.query(criteria);
        return template.count(query, Game.class);
    }

    // chuk revision:
    public List<Game> getPaginatedGameList(int limit, int pageNum) {
        Query query = new Query()
                .skip(limit * pageNum)
                .limit(limit);
        return template.find(query, Game.class, "games");
    }

    // Mongo Aggregation
    public List<Document> aggregateGamesByYear(int year) {
        MatchOperation matchRated= Aggregation.match(Criteria.where("year").is(year));

        ProjectionOperation projectOperations = Aggregation.project("gid","name")
                .andExclude("_id")
                .and("image").as("image_url"); // include column with alias

        GroupOperation groupOperation = Aggregation.group("Rated")
                .count().as("count")
                .push("Title").as("titles");

        SortOperation sortOperation = Aggregation.sort(Sort.by(Direction.ASC, "count"));

        Aggregation pipeline1= Aggregation.newAggregation(matchRated);
        Aggregation pipeline2= Aggregation.newAggregation(matchRated, projectOperations);
        Aggregation pipeline3= Aggregation.newAggregation(groupOperation, sortOperation);

        AggregationResults<Document> results1= template.aggregate(pipeline2, "games", Document.class);
        AggregationResults<Document> results2= template.aggregate(pipeline3, "movies", Document.class);
        List<Document> docList1 = results1.getMappedResults();
        List<Document> docList2 = results2.getMappedResults();

        return docList1;
    }

    /* concatenation
    
        db.movies.aggregate([
            {$project: {
                _id:0, 
                title:{
                    $concat: ["$Title"," (","$Rated",")"]
                },
                summary:"$Awards"
                }},
            {$sort:{title:1}}
        ])
     */
    public List<Document> aggregateMovies() {
        // method 1 - string concatenation
        ProjectionOperation projectOperations = Aggregation.project("Released")
                .andExclude("_id")
                .and("plot").as("summary") // include column with alias
                .and(
                    StringOperators.Concat.valueOf("Title").concat(" (")
                    .concatValueOf("Rated").concat(")")
                ).as("title");

        // method 2 - AggregationExpression
        ProjectionOperation projectOperations2 = Aggregation.project("Released")
                .andExclude("_id")
                .and("plot").as("summary") // include column with alias
                .and(
                    AggregationExpression.from(
                        MongoExpression.create("""
                            $concat: ["$Title"," (","$Rated",")"]
                        """) // using Mongo Query Language in Java, with MongoExpression
                    )
                ).as("title");

        SortOperation sortOperation = Aggregation.sort(Sort.by(Direction.ASC, "title"));
        Aggregation pipeline= Aggregation.newAggregation(projectOperations, sortOperation);
        AggregationResults<Document> results= template.aggregate(pipeline, "movies", Document.class);
        List<Document> docList = results.getMappedResults();
        return docList;
    }

    /* unwind array + group + sort
    
        db.movies.aggregate([
            {$unwind:"$Actors"},
            {$group: {
                _id:"$Actors",
                titles:{$push:"$Title"},
                titles_count:{$sum:1}
            }},
            {$sort:{"_id":1}}
        ])
     */
    public List<Document> aggregateMoviesUnwindByActor() {
        UnwindOperation unwindOperation = Aggregation.unwind("Actors");

        GroupOperation groupOperation = Aggregation.group("Actors")
                .push("Title").as("titles")
                .count().as("titles_count");

        SortOperation sortOperation = Aggregation.sort(Sort.by(Direction.DESC, "Actors"));
        Aggregation pipeline= Aggregation.newAggregation(unwindOperation, groupOperation, sortOperation);
        AggregationResults<Document> results= template.aggregate(pipeline, "movies", Document.class);
        List<Document> docList = results.getMappedResults();
        return docList;
    }

    /*bucket classification + unwind

        db.games.aggregate([
            {$bucket: {
                groupBy: "$year",
                boundaries: [1990,2000,2010,2020],
                default: "Others",
                output: {
                    count: {$sum:1},
                    titles: {$push:"$name"}
                }
            }},
            {$unwind:"$titles"}
        ])
     */
    public List<Document> aggregateGamesIntoYearBuckets() {
        BucketOperation bucketOperation = Aggregation.bucket("year")
                .withBoundaries(1990,2000,2010,2020)
                .withDefaultBucket("Others")
                .andOutputCount().as("count")
                .andOutput("name").push().as("titles");
        UnwindOperation unwindOperation = Aggregation.unwind("titles");

        Aggregation pipeline= Aggregation.newAggregation(bucketOperation,unwindOperation);
        AggregationResults<Document> results= template.aggregate(pipeline, "games", Document.class);
        List<Document> docList = results.getMappedResults();
        return docList;
    }

    /* embed a referenced document

        db.games.aggregate([
            {$match:{name:"Catan"}},
            {$lookup:{
                from:"comments",
                foreignField:"gid",
                localField:"gid",
                as:"reviews"
            }},
            {$unwind:"$reviews"}
        ])
     */
    public List<Document> aggregateGamesWithComments(String gameName) {
        MatchOperation match = Aggregation.match(Criteria.where("name").is(gameName));
        LookupOperation lookup = Aggregation.lookup("comments", "gid", "gid", "reviews");
        UnwindOperation unwind = Aggregation.unwind("reviews");

        Aggregation pipeline= Aggregation.newAggregation(match,lookup,unwind);
        AggregationResults<Document> results= template.aggregate(pipeline, "games", Document.class);
        List<Document> docList = results.getMappedResults();
        return docList;
    }

    /* aggregation -- embed referenced document
    
        db.games.aggregate([
            {$match: {
                name: {$regex: "ticket", $options: "i"}
            }},
            {$sort: {ranking:-1}},
            {$project:{_id:"$gid",name:1,year:1,ranking:1,users_rated:1,url:1,image:1}},
            {$lookup:{
                from:"comments",
                foreignField:"gid",
                localField:"_id",
                as:"comments",
                pipeline:[
                    {$sort:{rating:-1}},
                    {$limit:5}
                ]
            }}
        ])
     */
    public List<Document> getGamesWithComments(String name) {
        MatchOperation match = Aggregation.match(
            Criteria.where("name").regex(name, "i")
        );

        SortOperation sort = Aggregation.sort(Direction.DESC,"ranking");

        ProjectionOperation project = Aggregation.project()
            .and("gid").as("_id")
            .andInclude("name","year","ranking","users_rated","url","image");

        LookupOperation lookup = Aggregation.lookup()
                .from("comments")
                .localField("_id")
                .foreignField("gid")
                .pipeline(
                    Aggregation.sort(Direction.DESC, "rating"),
                    Aggregation.limit(5)
                )
                .as("comments");
           
        Aggregation pipeline = Aggregation.newAggregation(match,sort,project,lookup);
        return template.aggregate(pipeline, "games", Document.class).getMappedResults();
    }

}
