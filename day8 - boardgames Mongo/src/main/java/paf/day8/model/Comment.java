package paf.day8.model;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "comments")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    private String c_id;
    private String user;
    @Min(value = 0)
    @Max(value = 10)
    private Integer rating;
    private String c_text;
    private Integer gid;
    private Date posted;
    private String name;
    private List<EditedComment> edited;
}
