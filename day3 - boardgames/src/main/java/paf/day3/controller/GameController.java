package paf.day3.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import paf.day3.model.Game;
import paf.day3.service.GameService;

@Controller
@RequestMapping
public class GameController {
    
    @Autowired
    GameService svc;

    @GetMapping("/search")
    public ModelAndView showSearchGames(@RequestParam String searchString) {
        ModelAndView mav = new ModelAndView("search");
        List<Game> gameList = svc.getSearchedGames(searchString);
        mav.addObject("gameList", gameList);
        mav.addObject("searchString", searchString);
        return mav;
    }

}
