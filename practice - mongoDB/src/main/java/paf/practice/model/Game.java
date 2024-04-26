package paf.practice.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "games")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Game {
    @Id
    private ObjectId _id;
    private int gid;
    private String name;
    private int year;
    private int ranking;
    private int usersRated;
    private String url;
    private String image;
}
