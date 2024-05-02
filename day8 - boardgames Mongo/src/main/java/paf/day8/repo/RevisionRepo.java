package paf.day8.repo;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.MongoExpression;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.TextIndexDefinition;
import org.springframework.data.mongodb.core.index.TextIndexDefinition.TextIndexDefinitionBuilder;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.Update.Position;
import org.springframework.stereotype.Repository;

import paf.day8.model.Game;

@Repository
public class RevisionRepo {
    
    @Autowired
    private MongoTemplate template;

    // db.tvshows.findOne()
    public Optional<Document> findOne() {
        Query query = Query.query(Criteria.where("_id").exists(true));
        return Optional.ofNullable(template.findOne(query, Document.class));
    }

    // db.tvshows.find()
    public Optional<List<Document>> find() {
        Query query = Query.query(Criteria.where("_id").exists(true));
        return Optional.ofNullable(template.find(query, Document.class));
    }

    // db.tvshows.find({name: 'Fargo'})
    public Optional<List<Document>> findByName(String name) {
        Query query = Query.query(Criteria.where("name").is(name));
        return Optional.ofNullable(template.find(query, Document.class));
    }

    // // multiple search criteria:
    // db.tvshows.find({
    //     name: {$regex: 'battle', $options: 'i'}, // insensitive search
    //     status: 'Ended',
    //     "rating.average": { $gte: 5.0, $lte: 7.0}
    // })
    public Optional<List<Document>> findByMultipleCriteria(String name, String status, float greaterThan, float lessThan) {
        Criteria criteria = Criteria.where("name").regex(name, "i")
                .and("status").is(status)
                .and("rating.average").gte(greaterThan).lte(lessThan);
        Query query = Query.query(criteria);
        return Optional.ofNullable(template.find(query, Document.class));
    }

    // // search by _id:
    // db.tvshows.find({
    //     _id: ObjectId("6621d4137f50ea5760fbbc37")
    // })
    public Optional<Document> findByObjectId(ObjectId _id) {
        Query query = Query.query(Criteria.where("_id").is(_id));
        return Optional.ofNullable(template.findOne(query, Document.class));
    }

    // // logical operators:
    // db.tvshows.find({
    //     $or: [
    //         { year: { $gte: 2020 } },
    //         { year: { $lte: 1800 } }
    //     ]
    // })
    public Optional<List<Document>> findOrOperator(float greaterThan, float lessThan) {
        Criteria criteria = new Criteria().orOperator(
                Criteria.where("year").gte(greaterThan),
                Criteria.where("year").lte(lessThan)
                );
        Query query = Query.query(criteria);
        return Optional.ofNullable(template.find(query, Document.class));
    }

    // db.tvshows.find({
    //     genres: {
    //         $all: ['Drama','Thriller'] // must have ALL the listed attributes
    //     },
    //     // embedded attributes
    //     "schedule.days" : {
    //         $in: [ 'Friday','Saturday' ] // only need to have ONE OF the listed attributes
    //     },
    //     "rating.average": { $gte: 7.0},
    //     awards: { $exists: false }
    // })
    public Optional<List<Document>> findAllAndIn() {
        Criteria allCriteria = new Criteria("genres").all("Drama","Thriller");
        Criteria inCriteria = new Criteria("schedule.days").in("Friday","Saturday");
        Criteria gteCriteria = new Criteria("rating.average").gte(7.0);
        Criteria existsCriteria = new Criteria("awards").exists(false);

        Query query = Query.query(allCriteria)
                .addCriteria(inCriteria)
                .addCriteria(gteCriteria)
                .addCriteria(existsCriteria);
        return Optional.ofNullable(template.find(query, Document.class));
    }

    // // distinct
    // db.tvshows.distinct("type", {"rating.average": { $gte:7 }})
    public Optional<List<Document>> findDistinct() {
        Criteria criteria = Criteria.where("rating.average").gte(7);
        Query query = Query.query(criteria);
        return Optional.ofNullable(
            template.findDistinct(query, "type", "tvshows", Document.class));
    }

    // // count
    // db.tvshows.find({ "rating.average": { $gte: 5 } }).count()
    public Long getCount() {
        Criteria criteria = new Criteria("rating.average").gte(5);
        Query query = Query.query(criteria);
        return template.count(query, Document.class);
    }

    // // projection, sort, limit
    // db.tvshows.find({
    //     "rating.average": { $gte: 7.0 }
    // })
    // .projection({name:1, genres:1, _id:0}) // explicitly stating you dw _id
    // .sort({"rating.average":-1}) // descending order
    // .limit(10)
    public Optional<List<Document>> findProjectionSortLimit() {
        Criteria criteria = new Criteria("rating.average").gte(7);
        Query query = Query.query(criteria)
                .with(Sort.by(Direction.DESC, "rating.average"))
                .limit(10);
        query.fields().exclude("_id").include("name", "genres");
        return Optional.ofNullable(
            template.find(query, Document.class, "tvshows"));
    }

    // // create
    // db.persons.insert({
    //     name: "Mary",
    //     age: 25,
    //     gender: "F",
    //     hobbies: [ "sleeping","eating","coding" ]
    // });
    public void insertPersonA(Object person) {
        template.insert(person);
    }

    public Document insertPersonB(Object person) {
        Document document = new Document("name", "Mary")
                .append("age", 25)
                .append("gender", "F")
                .append("hobbies", Arrays.asList("sleeping", "eating", "coding"));
        return template.insert(document, "persons");
    }

    // // update
    // db.persons.updateOne(
    //     {gender : "F"},
    //     {
    //         $set: { name : "Emily2" },
    //         $inc: { age : 2 } // increment by 2
    //     }
    // );
    public long updatePerson() {
        Query query = new Query(Criteria.where("gender").is("F"));
        Update update = new Update()
                .set("name", "Emily2")
                .inc("age", 2);
        return template.updateFirst(query, update, "persons").getModifiedCount();
    }

    // // delete
    // db.persons.deleteOne({
    //     _id: ObjectId("66272616cb8054bb5fe125a9")
    // })
    public long deletePersonById(ObjectId id) {
        Query query = new Query(Criteria.where("_id").is(id));
        return template.remove(query, "persons").getDeletedCount();
    }

    // // create index
    // db.movies.createIndex({
    //     "$**" : "text"
    // });
    public void createTextIndexAll() {
        TextIndexDefinition textIndex = new TextIndexDefinitionBuilder()
                .onAllFields()
                .build();
        template.indexOps("movies").ensureIndex(textIndex);
    }

    // db.movies.createIndex({
    //     Title : "text"
    // });
    public void createTextIndex() {
        Index index = new Index()
                .named("title_text_index")
                .on("Title", Direction.DESC)
                .unique().background().sparse();
        template.indexOps("movies").ensureIndex(index);
    }

    // // drop indexes
    // db.movies.dropIndexes();
    public void dropAllIndexes() {
        template.indexOps("movies").dropAllIndexes();
    }

    // // search using index
    // db.movies.find({
    //     $text : {
    //         $search : "aVatAr",
    //         $caseSensitive : false
    //     }
    // });
    public Optional<List<Document>> findMoviesByTitle() {
        TextCriteria criteria = TextCriteria.forDefaultLanguage()
                .matching("aVatAr")
                .caseSensitive(false);
        return Optional.ofNullable(template.find(Query.query(criteria), Document.class));
    }

    // // search embedded document (condition applies to any document in the array)
    // db.inventory.find({
    //     $and : [
    //         { "instock.warehouse" : "Bedok" },
    //         { "instock.qty" : { $gte:15 } }
    //     ]
    // })
    public Optional<List<Document>> findEmbeddedAttributes() {
        Query query = new Query(Criteria
                .where("instock.warehouse").is("Bedok")
                .and("instock.qty").gte(15));
        return Optional.ofNullable(template.find(query, Document.class));
    }

    // // day 27 - slide 28 (element match)
    // db.inventory.find({
    //     instock : {
    //         $elemMatch: {warehouse: "Bedok", qty: {$gte:15}}
    //     }
    // })
    public Optional<List<Document>> findElemMatch() {
        Query query = new Query(Criteria.where("instock")
                .elemMatch(Criteria
                    .where("warehouse").is("Bedok")
                    .and("qty").gte(15)));
        return Optional.ofNullable(template.find(query, Document.class));
    }

    // // day 27 - slide 29. only first record is updated
    // db.inventory.update(
    //     {"instock.warehouse": "Ang Mo Kio"},
    //     {$inc: { "instock.$[].qty": 10 }}
    // );
    public long updateInventoryQtyByWarehouse() {
        Query query = new Query(Criteria.where("instock.warehouse").is("Ang Mo Kio"));
        Update update = new Update().inc("instock.$[].qty", 10);
        return template.updateMulti(query, update, Document.class).getModifiedCount();
    }

    // // day 27 - slide 30. push (add on) new embedded document
    // db.inventory.update(
    //     {item:"postcard"},
    //     {$push: {instock: {warehouse: "Ubi", qty:100 }}}
    // )
    public long updatePush() {
        Query query = new Query(Criteria.where("item").is("postcard"));
        Document instock = new Document()
                .append("warehouse", "Ubi")
                .append("qty", 100);
        Update update = new Update().push("instock", instock);
        return template.updateFirst(query, update, Document.class).getModifiedCount();
    }

    // // day 27 - slide 30. pop (take out) embedded document
    // db.inventory.update(
    //     {item: "postcard"},
    //     {$pop: {instock: -1}} // remove first element in the array. 1 is remove from the back
    // )
    public long updatePop() {
        Query query = new Query(Criteria.where("item").is("postcard"));
        Update update = new Update().pop("instock", Position.FIRST);
        return template.updateFirst(query, update, Document.class).getModifiedCount();
    }

    // // day 27 - slide 31. clean up data
    // db.inventory.updateMany(
    //     { "instock": { $elemMatch: { "warehouse": "Ang Mo Kio", "qty": { $lt: 0 } } } },
    //     { $set: { "instock.$[elem].qty" : 0 } },
    //     { arrayFilters: [ { "elem.warehouse" : "Ang Mo Kio", "elem.qty" : { $lt: 0 } } ] }
    //  )
    public long updateCleanUpData() {
        Query query = new Query(Criteria
                .where("instock")
                .elemMatch(Criteria
                    .where("warehouse").is("Ang Mo Kio")
                    .and("qty").lt(0)));
        Update update = new Update()
                .set("instock.$[elem].qty", 0)
                .filterArray(Criteria
                    .where("elem.warehouse").is("Ang Mo Kio")
                    .and("elem.qty").lt(0));
        return template.updateMulti(query, update, Document.class).getModifiedCount();
    }

    // // ============ AGGREGATION ============

    // // filter + project:
    // db.movies.aggregate([
    //     {$match: {Rated:"PG"}},
    //     {$project: {_id:0, Title:1, Year:1, Rated:1, summary:"$Awards"}}
    // ])
    public List<Document> aggregateMatchProject() {
        MatchOperation match = Aggregation.match(Criteria.where("Rated").is("PG"));
        ProjectionOperation project = Aggregation.project("Title","Year","Rated")
                .andExclude("_id")
                .and("Awards").as("summary");

        Aggregation pipeline= Aggregation.newAggregation(match, project);
        AggregationResults<Document> results= template.aggregate(pipeline, "movies", Document.class);
        List<Document> docList = results.getMappedResults();
        return docList;
    }

    // // group + sort:
    // db.movies.aggregate([
    //     {$group: {_id:"$Rated",count:{$sum:1},titles:{$push:"$Title"}}},
    //     {$sort: {count:-1}}
    // ])
    public List<Document> aggregateGroupSort() {
        GroupOperation group = Aggregation.group("Rated")
                .count().as("count")
                .push("Title").as("titles");
        SortOperation sort = Aggregation.sort(Sort.by(Direction.ASC, "count"));

        Aggregation pipeline= Aggregation.newAggregation(group, sort);
        AggregationResults<Document> results= template.aggregate(pipeline, "movies", Document.class);
        List<Document> docList = results.getMappedResults();
        return docList;
    }

    // // projection + concatenation:
    // db.movies.aggregate([
    //     {$project: {
    //         _id:0, 
    //         title:{
    //             $concat: ["$Title"," (","$Rated",")"]
    //         },
    //         summary:"$Awards"
    //         }},
    //     {$sort:{title:1}}
    // ])
    public List<Document> aggregateMovies() {
        // method 1 - string concatenation
        ProjectionOperation projectOperations1 = Aggregation.project("Released")
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
        Aggregation pipeline= Aggregation.newAggregation(projectOperations1, sortOperation);
        AggregationResults<Document> results= template.aggregate(pipeline, "movies", Document.class);
        List<Document> docList = results.getMappedResults();
        return docList;
    }

    // // unwind arrays + group + sort
    // db.movies.aggregate([
    //     {$unwind:"$Actors"},
    //     {$group: {
    //         _id:"$Actors",
    //         titles:{$push:"$Title"},
    //         titles_count:{$sum:1}
    //     }},
    //     {$sort:{"_id":1}}
    // ])
    public List<Document> aggregateMoviesUnwindByActor() {
        UnwindOperation unwind = Aggregation.unwind("Actors");
        GroupOperation group = Aggregation.group("Actors")
                .push("Title").as("titles")
                .count().as("titles_count");
        SortOperation sort = Aggregation.sort(Sort.by(Direction.DESC, "Actors"));

        Aggregation pipeline= Aggregation.newAggregation(unwind, group, sort);
        AggregationResults<Document> results= template.aggregate(pipeline, "movies", Document.class);
        List<Document> docList = results.getMappedResults();
        return docList;
    }

    // // bucket classification + unwind
    // db.movies.aggregate([
    //     {$unwind:"$Genre"},
    //     {$bucket: {
    //         groupBy: "$Genre",
    //         boundaries: ["Action","Adventure","Biography","Comedy","Drama","Fantasy","Horror","Sci-Fi"],
    //         default: "ZZZ",
    //         output: {
    //             count: {$sum:1}
    //             titles: {$push:{$concat:["$Title"," (","$Rated",")"]}}
    //         }
    //     }},
    //     {$unwind:"$titles"},
    //     {$sort:{titles:-1}},
    //     {$out:{db:"test",coll:"moviesTest"}}
    // ])
    public List<Document> aggregateGamesIntoYearBuckets() {
        UnwindOperation unwind1 = Aggregation.unwind("Genre");
        BucketOperation bucket = Aggregation.bucket("Genre")
                .withBoundaries("Action","Adventure","Biography","Comedy","Drama","Fantasy","Horror","Sci-Fi")
                .withDefaultBucket("ZZZ")
                .andOutputCount().as("count")
                .andOutput(StringOperators.Concat
                        .valueOf("Title")
                        .concat(" (")
                        .concatValueOf("Rated")
                        .concat(")"))
                    .push().as("titles");
        UnwindOperation unwind2 = Aggregation.unwind("titles");
        SortOperation sort = Aggregation.sort(Sort.by(Direction.DESC,"titles"));
        OutOperation out = Aggregation.out("moviesTest").in("test");

        Aggregation pipeline= Aggregation.newAggregation(unwind1,bucket,unwind2,sort,out);
        AggregationResults<Document> results= template.aggregate(pipeline, "movies", Document.class);
        List<Document> docList = results.getMappedResults();
        return docList;
    }

    // // embedding a reference document
    // db.games.aggregate([
    //     {$match:{name:"Catan"}},
    //     {$lookup:{
    //         from:"comments",
    //         foreignField:"gid",
    //         localField:"gid",
    //         as:"reviews"
    //     }},
    //     {$unwind:"$reviews"}
    // ])
    public List<Document> aggregateGamesWithComments(String gameName) {
        MatchOperation match = Aggregation.match(Criteria.where("name").is(gameName));
        LookupOperation lookup = Aggregation.lookup("comments", "gid", "gid", "reviews");
        UnwindOperation unwind = Aggregation.unwind("reviews");

        Aggregation pipeline= Aggregation.newAggregation(match,lookup,unwind);
        AggregationResults<Document> results= template.aggregate(pipeline, "games", Document.class);
        List<Document> docList = results.getMappedResults();
        return docList;
    }

    // // $lookup with pipeline
    // db.movies.aggregate([
    //     {$match: {Rated:"PG"}},
    //     {$lookup: {
    //         from: "reviews",
    //         foreignField: "movie_id",
    //         localField:"_id",
    //         pipeline: [
    //             {$sort: {user:1}},
    //             {$limit: 2},
    //             {$project: {_id:0,user:1,review:1}}
    //         ],
    //         as: "Reviews"
    //     }},
    //     {$project: {_id:1, Title:1,Year:1,Rated:1,Genre:1,Reviews:1}},
    //     {$sort: {Title:1}}
    // ]);
    public List<Document> aggregateLookupPipeline() {
        MatchOperation match = Aggregation.match(Criteria.where("Rated").is("PG"));
        LookupOperation lookup = Aggregation.lookup()
                .from("reviews")
                .localField("_id")
                .foreignField("movie_id")
                .pipeline(
                    Aggregation.sort(Sort.by(Direction.ASC, "user")),
                    Aggregation.limit(2),
                    Aggregation.project("user","review").andExclude("_id"))
                .as("Reviews");
        ProjectionOperation project = Aggregation.project("_id","Title","Year","Rated","Genre","Reviews");
        SortOperation sort = Aggregation.sort(Direction.ASC, "Title");

        Aggregation pipeline= Aggregation.newAggregation(match,lookup,project,sort);
        AggregationResults<Document> results= template.aggregate(pipeline, "movies", Document.class);
        List<Document> docList = results.getMappedResults();
        return docList;
    }

    // // converting data type (from String to Int/Bool)
    // db.movies.aggregate([
    //     {$set:{
    //         Year:{$toInt:"$Year"},
    //         Response:{$toBool:"$Response"}
    //     }}
    // ])
    public List<Document> aggregateSet() {
        ProjectionOperation project = Aggregation.project()
                .andExpression("{$convert: {input: '$Year', to: 'int'}}").as("Year")
                .andExpression("{$convert: {input: '$Response', to: 'bool'}}").as("Response");

        Aggregation pipeline= Aggregation.newAggregation(project);
        AggregationResults<Document> results= template.aggregate(pipeline, "movies", Document.class);
        List<Document> docList = results.getMappedResults();
        return docList;
    }

    // // converting data type (using $convert)
    // db.movies.aggregate([
    //     {$convert:{
    //         input:"",
    //         to:"",
    //         onError:"",
    //         onNull:""
    //     }}
    // ])
    public List<Document> aggregateConvert() {
        ProjectionOperation project = Aggregation.project()
                        .and(ConvertOperators
                            .valueOf("")
                            .convertTo("")
                            .onErrorReturn("")
                            .onNullReturn(""))
                        .as("");

        Aggregation pipeline= Aggregation.newAggregation(project);
        AggregationResults<Document> results= template.aggregate(pipeline, "movies", Document.class);
        List<Document> docList = results.getMappedResults();
        return docList;
    }

    // // ============ REVISION ===============

    // // filter and create new collection
    // db.listings_and_reviews.aggregate([
    //     {$match: {"address.country": {$regex: "australia", $options: "i"}}},
    //     {$out: {db: "bedandbreakfast", coll: "listings_test"}}
    // ]);

    // // drop collection
    // db.listings_and_reviews.drop();

    // // join collections, replace strings, and project as flatmap
    // db.listings.aggregate([
    //     {$unwind:"$reviews"},
    //     {$project:{
    //         _id:"$reviews._id",
    //         date:"$reviews.date",
    //         listing_id:"$reviews.listing_id",
    //         reviewer_name: {$replaceAll:{
    //             input: "$reviews.reviewer_name", 
    //             find: ",",
    //             replacement: ""
    //         }},
    //         comments: {$replaceAll:{
    //             input: {$replaceAll:{
    //                 input: "$reviews.comments",
    //                 find: "\n",
    //                 replacement: ""
    //             }},
    //             find: "\r",
    //             replacement: ""
    //         }}        
    //     }},
    //     {$out: {db: "bedandbreakfast", coll: "reviews"}}
    // ])

    // // remove a column from collection
    // db.listings.updateMany(
    // {},
    // {$unset: {reviews: 1}}
    // )

    // paginated find list
    public List<Game> getPaginatedGameList(int limit, int pageNum) {
        Query query = new Query()
                .skip(limit * pageNum)
                .limit(limit);
        return template.find(query, Game.class, "games");
    }

}
