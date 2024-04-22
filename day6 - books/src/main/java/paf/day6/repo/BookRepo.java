package paf.day6.repo;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import paf.day6.Constants;

@Repository
public class BookRepo {

    @Autowired @Qualifier(Constants.LIBRARY_DB)
    private MongoTemplate template;

    /*
     * db.books.find({
     *      title: { $regex: 'harry', $options:'i' }
     * })
     */
    public List<Document> findBooksByTitle(String title) {
        Criteria criteria = Criteria
                .where(Constants.F_TITLE).regex(title, "i");
        Query query = Query.query(criteria);
        return template.find(query, Document.class, Constants.C_BOOKS);
    }

    /*
     * db.books.find({
     *      bookID: 15867
     * })
     */
    public Document findBookByBookId(int bookId) {
        Criteria criteria = Criteria
                .where(Constants.F_BOOK_ID).is(bookId);
        Query query = Query.query(criteria);
        return template.findOne(query, Document.class, Constants.C_BOOKS);
    }

}
