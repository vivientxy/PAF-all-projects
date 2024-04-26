package paf.day8.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import paf.day8.model.Game;
import paf.day8.repo.GameRepo;

@Controller
public class GameRestController {
    
    @Autowired
    GameRepo repo;

    // @GetMapping("/games")
    // public ResponseEntity<String> getGamesByName(
    //             @RequestParam(defaultValue = "25") int limit, 
    //             @RequestParam(defaultValue = "0") int offset,
    //             @RequestParam String searchString) {
    //     List<Game> gameList = repo.getGamesByName(searchString, limit, offset);
    //     JsonArray gameListJson = Json.createArrayBuilder().
    //     Json.createObjectBuilder().add("games", results);

    //     return new ResponseEntity<String>(null, null);
    // }

    // @PostMapping("/review")
    // public 

}
