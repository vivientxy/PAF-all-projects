package paf.day8.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import paf.day8.model.Comment;
import paf.day8.model.EditedComment;
import paf.day8.model.Game;
import paf.day8.repo.GameRepo;

@Service
public class GameService {

    @Autowired
    GameRepo repo;

    // Day 26 task (a)
    public JsonObject getGamesByName(String name, int limit, int offset) {
        List<Game> gameList = repo.getGamesByName(name, limit, offset);
        return getJson(gameList, name, limit, offset);
    }

    // Day 26 task (b)
    public JsonObject getGamesByNameAndRank(String name, int limit, int offset) {
        List<Game> gameList = repo.getGamesByNameAndRank(name, limit, offset);
        return getJson(gameList, name, limit, offset);
    }

    // repeatable code: Day 26 task (a) and (b)
    private JsonObject getJson(List<Game> games, String name, int limit, int offset) {
        JsonArrayBuilder gamesArrBuilder = Json.createArrayBuilder();
        for (Game game : games) {
            JsonObjectBuilder objBuilder = Json.createObjectBuilder()
                    .add("game_id", game.getGid())
                    .add("name", game.getName());
            gamesArrBuilder.add(objBuilder);
        }
        return Json.createObjectBuilder()
                .add("games", gamesArrBuilder)
                .add("offset", offset)
                .add("limit", limit)
                .add("total", repo.getGamesCountByName(name))
                .add("timestamp", new Date().toString())
                .build();
    }
    
    // Day 26 task (c)
    public Boolean isGameIdExist(Integer game_id) {
        return null != repo.getGameById(game_id);
    }

    // Day 26 task (c)
    public JsonObject getGameById(Integer game_id) {
        Game g = repo.getGameById(game_id);
        return Json.createObjectBuilder()
            .add("game_id", g.getGid())
            .add("name", g.getName())
            .add("year", g.getYear())
            .add("ranking", g.getRanking())
            .add("users_rated", g.getUsers_rated())
            .add("url", g.getUrl())
            .add("thumbnail", g.getImage())
            .add("timestamp", new Date().toString())
            .build();
    }

    // Day 27 task (a)
    public JsonObject saveComment(Comment comment) {
        Integer gameId = comment.getGid();
        String gameName = repo.getGameById(gameId).getName();
        String c_id = UUID.randomUUID().toString().substring(0, 8);

        comment.setC_id(c_id);
        comment.setName(gameName);
        comment.setPosted(new Date());

        Comment c = repo.saveComment(comment);
        return Json.createObjectBuilder()
            .add("user", c.getUser())
            .add("rating", c.getRating())
            .add("comment", c.getC_text())
            .add("ID", c.getGid())
            .add("posted", c.getPosted().toString())
            .add("name", c.getName())
            .build();
    }

    // Day 27 task (b)
    public long updateComment(String c_id, Integer rating, String comment) {
        return repo.updateComment(c_id, rating, comment);
    }

    // Day 27 task (c)
    public JsonObject getComment(String c_id) {
        Comment c = repo.getComment(c_id);
        if (null == c)
            return null;

        boolean isEdited = c.getEdited().size() > 0;

        return getCommentJsonObjectBuilder(c)
            .add("edited", isEdited)
            .build();
    }

    // Day 27 task (d)
    public JsonObject getCommentHistory(String c_id) {
        Comment c = repo.getComment(c_id);
        if (null == c)
            return null;

        List<EditedComment> edits = c.getEdited();
        JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
        for (EditedComment edit : edits) {
            JsonObjectBuilder objBuilder = Json.createObjectBuilder()
                    .add("comment", edit.getC_text())
                    .add("rating", edit.getRating())
                    .add("posted", edit.getPosted().toString());
            arrBuilder.add(objBuilder);
        }
        
        return getCommentJsonObjectBuilder(c)
            .add("edited", arrBuilder)
            .build();
    }

    // repeatable code: Day 27 task (c) and (d) 
    private JsonObjectBuilder getCommentJsonObjectBuilder(Comment c) {
        String name = repo.getGameById(c.getGid()).getName();
        boolean isEdited = c.getEdited().size() > 0;
        Integer rating = c.getRating();
        String comment = c.getC_text();
        Date posted = c.getPosted();

        if (isEdited) {
            List<EditedComment> edited = c.getEdited();
            EditedComment latestComment = edited.get(edited.size() - 1);
            rating = latestComment.getRating();
            comment = latestComment.getC_text();
            posted = latestComment.getPosted();
        }

        return Json.createObjectBuilder()
            .add("user", c.getUser())
            .add("rating", rating) // get from edited
            .add("comment", comment) // get from edited
            .add("ID", c.getGid())
            .add("posted", posted.toString()) // get from edited
            .add("name", name)
            .add("timestamp", new Date().toString());
    }

    // Day 28 task (a)
    public JsonObject getGameReviews(Integer gid) {
        Game game = repo.getGameById(gid);
        if (null == game)
            return null;

        List<Comment> comments = repo.getGameReviews(gid);
        JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
        for (Comment comment : comments) {
            StringBuilder strBuilder = new StringBuilder("/review/")
                    .append(comment.getC_id());
            arrBuilder.add(strBuilder.toString());
        }
        
        return Json.createObjectBuilder()
            .add("game_id", gid)
            .add("name", game.getName())
            .add("year", game.getYear())
            .add("rank", game.getRanking())
            .add("users_rated", game.getUsers_rated())
            .add("url", game.getUrl())
            .add("thumbnail", game.getImage())
            .add("reviews", arrBuilder)
            .add("timestamp", new Date().toString())
            .build();
    }

    // Day 28 task (b)
    public JsonObject getGameReviews(String highestOrLowest) {
        List<Document> docs = repo.getGameReviews(highestOrLowest);
        JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
        for (Document doc : docs) {
            JsonObjectBuilder objBuilder = Json.createObjectBuilder()
                .add("_id", doc.get("_id").toString())
                .add("name", doc.get("name").toString())
                .add("rating", doc.get("rating").toString())
                .add("user", doc.get("user").toString())
                .add("comment", doc.get("comment").toString())
                .add("review_id", doc.get("review_id").toString());
            arrBuilder.add(objBuilder);
        }
        return Json.createObjectBuilder()
                .add("rating", highestOrLowest)
                .add("games", arrBuilder)
                .add("timestamp", new Date().toString())
                .build();
    }
    
}
