package paf.day2.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import paf.day2.model.RSVP;
import paf.day2.service.RSVPService;

@RestController
@RequestMapping("/api/rsvps")
public class RSVPRestController {

    @Autowired
    private RSVPService svc;

    @GetMapping
    public ResponseEntity<List<RSVP>> getAll() {
        List<RSVP> result = svc.findAll();
        if (result.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> getCount() {
        return new ResponseEntity<>(svc.count(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Boolean> saveRSVP(@RequestBody RSVP rsvp) {
        Boolean status = false;
        System.out.println(rsvp.getFullName());
        System.out.println(rsvp.getEmail());
        System.out.println(rsvp.getPhone());
        System.out.println(rsvp.getConfirmationDate());
        System.out.println(rsvp.getConfirmationDate().getTime());
        System.out.println(rsvp.getComment());
        status = svc.saveRSVP(rsvp);
        if (status)
            return new ResponseEntity<>(status, HttpStatus.CREATED);
        return new ResponseEntity<>(status, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping("/{email}")
    public ResponseEntity<Boolean> updateRSVP(@PathVariable String email, @RequestBody RSVP rsvp) {
        Boolean status = false;
        rsvp.setEmail(email);
        System.out.println(rsvp.getFullName());
        System.out.println(rsvp.getEmail());
        System.out.println(rsvp.getPhone());
        System.out.println(rsvp.getConfirmationDate());
        System.out.println(rsvp.getConfirmationDate().getTime());
        System.out.println(rsvp.getComment());
        status = svc.updateRSVP(rsvp);
        if (status)
            return new ResponseEntity<>(status, HttpStatus.CREATED);
        return new ResponseEntity<>(status, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
}
