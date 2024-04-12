package paf.day3.repo;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import paf.day3.model.Game;

@Repository
public class GameRepository implements Queries {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<Game> getSearchedGames(String searchString) {
        List<Game> gameList = new LinkedList<>();
        searchString = "%" + searchString + "%";
        System.out.println(">>>>> YOUR SEARCH STRING IS: " + searchString);
        final SqlRowSet rs = jdbcTemplate.queryForRowSet(SEARCH_GAMES, searchString);
        while (rs.next()) {
            Game game = new Game();
            game.setName(rs.getString("name"));
            game.setNumOfReviews(rs.getInt("num_of_reviews"));
            game.setAvgRating(rs.getDouble("average_rating"));
            gameList.add(game);
        }
        return Collections.unmodifiableList(gameList);
    }
    
}
