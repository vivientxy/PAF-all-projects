package paf.day4workshop.model;

import java.util.Date;
import java.util.List;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Order {

    private Integer orderId;
    private Date orderDate;
    private String customerName;
    private String shipAddress;
    private String notes;
    private Float tax;
    private List<OrderDetail> orderDetails;

    public Order() {
        this.orderDate = new Date();
    }

    public JsonObject toJson() {
        JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
        for (OrderDetail orderDetail : orderDetails) {
            JsonObjectBuilder objBuilder = Json.createObjectBuilder()
                .add("product", orderDetail.getProduct())
                .add("unit_price", orderDetail.getUnitPrice())
                .add("discount", orderDetail.getDiscount())
                .add("quantity", orderDetail.getQuantity());
            arrBuilder.add(objBuilder);
        }
        return Json.createObjectBuilder()
            .add("customer_name", customerName)
            .add("ship_address", shipAddress)
            .add("notes", notes)
            .add("tax", tax)
            .add("line_items", arrBuilder)
            .build();
    }
    
}
