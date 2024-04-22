package paf.day6.repo;

// import java.util.List;

// import org.bson.Document;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.beans.factory.annotation.Qualifier;
// import org.springframework.data.domain.Sort;
// import org.springframework.data.domain.Sort.Direction;
// import org.springframework.data.mongodb.core.MongoTemplate;
// import org.springframework.data.mongodb.core.query.Criteria;
// import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

// import paf.day6.Constants;

@Repository
public class TVShowRepo {

    // @Autowired @Qualifier(Constants.SHOWS_DB)
    // private MongoTemplate template;

    // /*
    //  * db.tvshows.find({
    //  *      name: { $regex: 'name', $options: 'i' },
    //  *      genres: { $all: [ 'Drama','Thriller' ]}
    //  * })
    //  * .projection({ name:1, genres:1 })
    //  * .sort({ name:-1 })
    //  * .limit(5)
    //  */
    // public List<Document> findShowsByName(String name) {
    //     Criteria criteria = Criteria
    //             .where(Constants.F_NAME).regex(name, "i")
    //             .and("genres").all("Drama","Thriller");
    //     Query query = Query.query(criteria)
    //             .with(Sort.by(Direction.DESC, Constants.F_NAME))
    //             .limit(5);
    //     query.fields().include(Constants.F_NAME, Constants.F_GENRES); // projection
    //     return template.find(query, Document.class, Constants.C_TVSHOWS);
    // }

    // /*
    //  * db.tvshows.find({
    //  *      language: { $regex: 'language', $options: 'i' },
    //  * }).count()
    //  */
    // public Long countShowsByLanguage(String language) {
    //     Criteria criteria = Criteria
    //             .where(Constants.F_LANGUAGE).regex(language, "i");
    //     Query query = Query.query(criteria);
    //     return template.count(query, Constants.C_TVSHOWS);
    // }

    // /*
    //  * db.tvshows.distinct("type", {"rating.average": { $gte:7 }})
    //  */
    // public List<String> getTypesByRating(float rating) {
    //     Criteria criteria = Criteria
    //             .where(Constants.F_AVERAGE_RATING).gte(rating);
    //     Query query = Query.query(criteria);
    //     return template.findDistinct(query, Constants.F_TYPE, Constants.C_TVSHOWS, String.class);
    // }
}
