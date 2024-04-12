package paf.day2.model;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RSVP {

    private int id;
    private String fullName;
    private String email;
    private String phone;
    private Date confirmationDate;
    private String comment;
    
}
