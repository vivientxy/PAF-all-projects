package paf.practice.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player {
    private String username;
    private String password;
    private String securityQuestion;
    private String securityAnswer;
    private Float wallet;
}
