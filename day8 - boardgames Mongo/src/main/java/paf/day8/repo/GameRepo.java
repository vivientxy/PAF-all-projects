package paf.day8.repo;

import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.LimitOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.UnwindOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

import paf.day8.model.Comment;
import paf.day8.model.Game;

@Repository
public class GameRepo {

    @Autowired
    private MongoTemplate template;

    // day 26 task (a)
    /*
        db.games.find({
            name: {$regex:"name",$options:"i"}
        })
        .skip(0)
        .limit(10);
     */
    public List<Game> getGamesByName(String name, int limit, int offset) {
        Query query = Query.query(Criteria.where("name").regex(name, "i"))
                .skip(offset)
                .limit(limit);
        return template.find(query, Game.class, "games");
    }

    // day 26 task (a) and (b)
    /*
        db.games.count({
            name: {$regex:"name",$options:"i"}
        })
     */
    public long getGamesCountByName(String name) {
        Query query = Query.query(Criteria.where("name").regex(name, "i"));
        return template.count(query, Game.class, "games");
    }

    // day 26 task (b)
    /*
        db.games.find({
            name: {$regex:"name",$options:"i"}
        })
        .sort({ranking:1})
        .skip(0)
        .limit(10);
     */
    public List<Game> getGamesByNameAndRank(String name, int limit, int offset) {
        Query query = Query.query(Criteria.where("name").regex(name, "i"))
                .with(Sort.by(Direction.ASC,"ranking"))
                .skip(offset)
                .limit(limit);
        return template.find(query, Game.class, "games");
    }

    // day 26 task (c)
    /*
        db.games.find({
            _id: ObjectId("65b32e122da1824ea35a3b77")
        })
     */
    public Game getGameById(Integer game_id) {
        Query query = Query.query(Criteria.where("gid").is(game_id));
        return template.findOne(query, Game.class, "games");
    }

    // day 27 task (a)
    public Comment saveComment(Comment comment) {
        return template.save(comment, "comments");
    }

    // day 27 task (b)
    /*
        db.comments.updateOne(
            {c_id: "091910bb")},
            {$push: {edited: {rating: 7, c_text: "hello this is updated", posted: new Date}}}
        )
     */
    public long updateComment(String c_id, Integer rating, String comment) {
        Query query = Query.query(Criteria.where("c_id").is(c_id));
        Update updateOps = new Update()
                .addToSet("edited", 
                    new Document("rating", rating)
                    .append("c_text", comment)
                    .append("posted", new Date()));
        UpdateResult updateResult = template.updateFirst(query, updateOps, Comment.class, "comments");
        return updateResult.getModifiedCount();
    }

    // day 27 task (c)
    public Comment getComment(String c_id) {
        Query query = Query.query(Criteria.where("c_id").is(c_id));
        return template.findOne(query, Comment.class, "comments");
    }

    // day 28 task (a)
    /*
        db.comments.find({
            gid: 150
        })
     */
    public List<Comment> getGameReviews(Integer gid) {
        Query query = Query.query(Criteria.where("gid").is(gid));
        return template.find(query, Comment.class, "comments");
    }

    // day 28 task (b)
    /*
        db.games.aggregate([
            {$lookup:{
                from: "comments",
                foreignField:"gid",
                localField:"gid",
                pipeline: [
                    {$sort: {rating: -1}},
                    {$limit:1}
                ],
                as:"reviews"
            }},
            {$unwind:"$reviews"},
            {$project: {
                gid: 1, 
                name: 1,
                rating: "$reviews.rating",
                user: "$reviews.user",
                comment: "$reviews.c_text",
                review_id: "$reviews.c_id"
            }}
        ])
     */
    public List<Document> getGameReviews(String highestOrLowest) {
        Direction direction = Direction.DESC;
        if ("lowest".equalsIgnoreCase(highestOrLowest))
            direction = Direction.ASC;
        LookupOperation lookup = Aggregation.lookup()
                .from("comments")
                .localField("gid")
                .foreignField("gid")
                .pipeline(
                    Aggregation.sort(Sort.by(direction, "rating")), 
                    Aggregation.limit(1))
                .as("reviews");
        UnwindOperation unwind = Aggregation.unwind("reviews");
        ProjectionOperation project = Aggregation.project("gid","name")
            .andExclude("_id")
            .and("gid").as("_id")
            .and("name").as("name")
            .and("reviews.rating").as("rating")
            .and("reviews.user").as("user")
            .and("reviews.c_text").as("comment")
            .and("reviews.c_id").as("review_id");

        // limit to 50 if not takes too long
        LimitOperation limit = Aggregation.limit(50);

        Aggregation pipeline= Aggregation.newAggregation(limit,lookup,unwind,project);
        AggregationResults<Document> results= template.aggregate(pipeline, "games", Document.class);
        List<Document> docList = results.getMappedResults();
        return docList;
    }

    /* ======================================================================================== */
    // CRUD
    public List<Game> getAllGames() {
        return template.findAll(Game.class, "games");
    }

    public Game createGame(Game g) {
        return template.save(g);
    }

    public long updateGame(Game g) {
        Query query = Query.query(Criteria.where("_id").is(g.getGid()));
        Update updateOps = new Update()
                .set("name", g.getName())
                .set("ranking", g.getRanking())
                .set("usersRated", g.getUsers_rated())
                .set("year", g.getYear());
        UpdateResult updateResult = template.updateMulti(query, updateOps, Game.class, "games");
        return updateResult.getModifiedCount();
    }

    public long deleteGame(int id) {
        Query query = Query.query(Criteria.where("_id").is(id));
        DeleteResult deleteResult = template.remove(query, "games");
        return deleteResult.getDeletedCount();
    }
    
}
