package paf.day8.model;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.json.Json;
import jakarta.json.JsonObjectBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "comments")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditedComment {
    private String cid;
    private int rating;
    private String comment;
    private LocalDateTime posted;

    public JsonObjectBuilder toJsonBuilder() {
        return Json.createObjectBuilder()
                .add("cid", getCid())
                .add("rating", getRating())
                .add("comment", getComment())
                .add("posted", getPosted().toString());
    }
}
