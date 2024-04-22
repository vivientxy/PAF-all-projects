package paf.day2.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import paf.day2.model.RSVP;
import paf.day2.repo.RSVPRepository;

@Service
public class RSVPService {
    
    @Autowired
    private RSVPRepository repo;

    public int count() {
        return repo.count();
    }

    public List<RSVP> findAll() {
        return repo.findAll();
    }

    public Boolean saveRSVP(RSVP rsvp) {
        return repo.saveRSVP(rsvp);
    }

    public Boolean updateRSVP(RSVP rsvp) {
        boolean isRSVPExist = false;
        if (!isRSVPExist) {
            repo.updateRSVP(rsvp);
            return true;
        }
        return false;
    }

}
