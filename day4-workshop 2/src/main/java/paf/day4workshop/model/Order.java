package paf.day4workshop.model;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    private Integer orderId;
    private Date orderDate;
    private String customerName;
    private String shipAddress;
    private String notes;
    private Float tax;
    private List<OrderDetail> orderDetails;
    
}
