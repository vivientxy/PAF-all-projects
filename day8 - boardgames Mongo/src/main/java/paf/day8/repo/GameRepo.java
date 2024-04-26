package paf.day8.repo;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperationContext;
import org.springframework.data.mongodb.core.aggregation.AggregationPipeline;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

import paf.day8.model.Game;

@Repository
public class GameRepo {

    @Autowired
    private MongoTemplate template;

    public List<Game> getAllGames() {
        return template.findAll(Game.class, "games");
    }

    public List<Game> getPaginatedGameList(int limit, int pageNum) {
        Query query = new Query()
                .skip(limit * pageNum)
                .limit(limit);
        return template.find(query, Game.class, "games");
    }

    public Game createGame(Game g) {
        return template.save(g);
    }

    public long updateGame(Game g) {
        Query query = Query.query(Criteria.where("_id").is(g.getGid()));
        Update updateOps = new Update()
                .set("name", g.getName())
                .set("ranking", g.getRanking())
                .set("usersRated", g.getUsersRated())
                .set("year", g.getYear());
        UpdateResult updateResult = template.updateMulti(query, updateOps, Game.class, "games");
        return updateResult.getModifiedCount();
    }

    public long deleteGame(int id) {
        Query query = Query.query(Criteria.where("_id").is(id));
        DeleteResult deleteResult = template.remove(query, "games");
        return deleteResult.getDeletedCount();
    }

    public List<Game> getGamesByName(String name, int limit, int offset) {
        Query query = Query.query(Criteria.where("name").regex(name, "i"))
                .skip(offset)
                .limit(limit);
        return template.find(query, Game.class);
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
