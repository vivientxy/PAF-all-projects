package paf.day8.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "games")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Game {
    @Id
    private int gid;
    private String name;
    private int year;
    private int ranking;
    private int usersRated;
    private String url;
    private String image;

    public JsonObject toJson() {
        return Json.createObjectBuilder()
            .add("gid", gid)
            .add("name", name)
            .add("year", year)
            .add("ranking", ranking)
            .add("users_rated", usersRated)
            .add("url", url)
            .add("image", image)
            .build();
    }
}
