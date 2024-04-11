package paf.day2.repo;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import paf.day2.model.RSVP;

@Repository
public class RSVPRepository implements RSVPQueries {

    @Autowired
    private JdbcTemplate template;
    
    public int count() {
        return template.queryForObject(COUNT_RSVP, Integer.class);
    }

    public List<RSVP> findAll() {
        List<RSVP> result = new LinkedList<>();
        result = template.query(ALL_RSVP, BeanPropertyRowMapper.newInstance(RSVP.class));
        return result;
    }

    public Boolean saveRSVP(RSVP rsvp) {
        int insertResult = template.update(INSERT_RSVP, rsvp.getFullName(), rsvp.getEmail(), rsvp.getPhone(), rsvp.getConfirmationDate(), rsvp.getComment());
        return insertResult > 0 ? true : false;
    }

    public Boolean updateRSVP(RSVP rsvp) {
        int updateResult = template.update(UPDATE_RSVP, rsvp.getPhone(), rsvp.getConfirmationDate(), rsvp.getComment(), rsvp.getEmail());
        return updateResult > 0 ? true : false;
    }

}
