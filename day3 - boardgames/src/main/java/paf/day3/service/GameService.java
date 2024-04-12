package paf.day3.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import paf.day3.model.Game;
import paf.day3.repo.GameRepository;

@Service
public class GameService {

    @Autowired
    GameRepository repo;

    public List<Game> getSearchedGames(String searchString) {
        return repo.getSearchedGames(searchString);
    }    
}
