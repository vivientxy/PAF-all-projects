package paf.day8.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.json.JsonObject;
import paf.day8.model.Comment;
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

    // Day 27 task (a)
    @PostMapping("/review")
    public ResponseEntity<String> postReview(@ModelAttribute Comment comment) {
        if (!svc.isGameIdExist(comment.getGid()))
            return new ResponseEntity<String>("Game ID does not exist", HttpStatusCode.valueOf(404));
        JsonObject json = svc.saveComment(comment);
        return new ResponseEntity<String>(json.toString(), HttpStatus.OK);
    }

    // Day 27 task (b)
    @PutMapping("/review/{review_id}")
    public ResponseEntity<String> updateReview(@PathVariable String review_id, Integer rating, @RequestParam MultiValueMap<String,String> mvm) {
        String comment = null;
        if (mvm.containsKey("comment"))
            comment = mvm.getFirst("comment");
        if (svc.updateComment(review_id, rating, comment) == 0)
            return new ResponseEntity<String>("Review ID does not exist", HttpStatusCode.valueOf(404));
        return new ResponseEntity<String>("Review updated successfully", HttpStatus.OK);
    }

    // Day 27 task (c)
    @GetMapping("/review/{review_id}")
    public ResponseEntity<String> getReview(@PathVariable String review_id) {
        JsonObject json = svc.getComment(review_id);
        if (null == json)
            return new ResponseEntity<String>("Review ID does not exist", HttpStatusCode.valueOf(404));
        return new ResponseEntity<String>(json.toString(), HttpStatus.OK);
    }

    // Day 27 task (d)
    @GetMapping("/review/{review_id}/history")
    public ResponseEntity<String> getReviewHistory(@PathVariable String review_id) {
        JsonObject json = svc.getCommentHistory(review_id);
        if (null == json)
            return new ResponseEntity<String>("Review ID does not exist", HttpStatusCode.valueOf(404));
        return new ResponseEntity<String>(json.toString(), HttpStatus.OK);
    }
}
