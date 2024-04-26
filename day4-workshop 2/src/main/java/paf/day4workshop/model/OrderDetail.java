package paf.day4workshop.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetail {

    private Integer id;
    private String product;
    private Float unitPrice;
    private Float discount;
    private Integer quantity;

}
