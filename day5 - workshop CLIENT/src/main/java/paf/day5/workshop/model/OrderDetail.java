package paf.day5.workshop.model;

import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
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
    private Integer orderId;

    public OrderDetail(JsonValue json) {
        JsonObject obj = json.asJsonObject();
        this.product = obj.getString("product");
        this.unitPrice = (float) obj.getJsonNumber("unit_price").doubleValue();
        this.discount = (float) obj.getJsonNumber("discount").doubleValue();
        this.quantity = obj.getInt("quantity");
    }

}
