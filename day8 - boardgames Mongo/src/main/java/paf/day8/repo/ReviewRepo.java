package paf.day8.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.mongodb.client.result.UpdateResult;

import paf.day8.model.Review;

@Repository
public class ReviewRepo {

    @Autowired
    private MongoTemplate template;

    public Review getReview(int reviewId) {
        Query query = Query.query(Criteria.where("reviewId").is(reviewId));
        return template.findOne(query, Review.class);
    }
    
    public long updateReview(Review r) {
        Query query = Query.query(Criteria.where("_id").is(r.getCid()));
        Update updateOps = new Update()
            .set("rating", r.getRating())
            .set("comment", r.getComment())
            .set("posted", r.getPosted())
            .set("edited", r.getEdited());
        UpdateResult updateResult = template.updateMulti(query, updateOps, Review.class, "comments");
        return updateResult.getModifiedCount();
    }

}
