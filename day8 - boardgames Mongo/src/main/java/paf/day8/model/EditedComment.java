package paf.day8.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditedComment {
    private Integer rating;
    private String c_text;
    private Date posted;
}
