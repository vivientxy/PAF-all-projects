package paf.day8.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "comments")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Review extends EditedComment {
    @Id
    private int reviewId;
    private int gameId;
    private String gameName;
    private String user;
    private List<EditedComment> edited;

    public JsonObject toJson() {
        JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
        List<JsonObjectBuilder> editedComments = this.getEdited().stream()
            .map(t -> toJsonBuilder())
            .toList();
        for (JsonObjectBuilder j : editedComments) {
            arrBuilder.add(j);
        }
        return Json.createObjectBuilder()
                .add("gameId", getGameId())
                .add("gameName", getGameName())
                .add("user", getUser())
                .add("rating", getRating())
                .add("comment", getComment())
                .add("posted", getPosted().toString())
                .add("edited", arrBuilder)
                .build();
    }

}
