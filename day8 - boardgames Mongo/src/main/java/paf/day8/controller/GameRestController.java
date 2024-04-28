package paf.day8.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.json.JsonObject;
import paf.day8.service.GameService;

@Controller
public class GameRestController {
    
    @Autowired
    GameService svc;

    // Day 26 task (a)
    @GetMapping("/games")
    public ResponseEntity<String> getGamesByName(
                @RequestParam(defaultValue = "25") int limit, 
                @RequestParam(defaultValue = "0") int offset,
                @RequestParam String name) {
        JsonObject json = svc.getGamesByName(name, limit, offset);
        return new ResponseEntity<String>(json.toString(), HttpStatus.OK);
    }

    // Day 26 task (b)
    @GetMapping("/games/rank")
    public ResponseEntity<String> getGamesByRank(
                @RequestParam(defaultValue = "25") int limit, 
                @RequestParam(defaultValue = "0") int offset,
                @RequestParam String name) {
        JsonObject json = svc.getGamesByNameAndRank(name, limit, offset);
        return new ResponseEntity<String>(json.toString(), HttpStatus.OK);
    }

    // Day 26 task (c)
    @GetMapping("/game/{game_id}")
    public ResponseEntity<String> getGameDetail(
                @PathVariable Integer game_id) {
        if (!svc.isGameIdExist(game_id))
            return new ResponseEntity<String>("Game ID does not exist", HttpStatusCode.valueOf(404));
        JsonObject json = svc.getGameById(game_id);
        return new ResponseEntity<String>(json.toString(), HttpStatus.OK);
    }
}
