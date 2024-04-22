package paf.day6.model;

import org.bson.Document;
import org.bson.types.ObjectId;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Book {

    private ObjectId _id;
    private int bookID;
    private String title;
    private String authors;
    private float averageRating;
    private Object isbn;
    private Long isbn13;
    private String languageCode;
    private int numPages;
    private int ratingsCount;
    private int textReviewsCount;
    private String publicationDate;
    private String publisher;

    public Book(Document doc) {
        this._id = doc.getObjectId("_id");
        this.bookID = doc.getInteger("bookID");
        this.title = doc.getString("title");
        this.authors = doc.getString("authors");
        this.averageRating = doc.getDouble("average_rating").floatValue();
        this.isbn = doc.get("isbn");
        this.isbn13 = doc.getLong("isbn13");
        this.languageCode = doc.getString("language_code");
        this.numPages = doc.getInteger("num_pages");
        this.ratingsCount = doc.getInteger("ratings_count");
        this.textReviewsCount = doc.getInteger("text_reviews_count");
        this.publicationDate = doc.getString("publication_date");
        this.publisher = doc.getString("publisher");
    }
    
}
