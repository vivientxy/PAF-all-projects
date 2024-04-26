package paf.practice.model;

import java.util.Date;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Trade {
    private Integer tradeId;
    private String ticker;
    private Integer quantity;
    private Float price;
    private String direction;
    private String username;
    private Date tradeDate;
}
