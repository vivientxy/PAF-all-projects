package paf.day8.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
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

    // Day 26 task (c)
    public Boolean isGameIdExist(Integer game_id) {
        return null != repo.getGameById(game_id);
    }

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

    // repeatable code:
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
    
}
