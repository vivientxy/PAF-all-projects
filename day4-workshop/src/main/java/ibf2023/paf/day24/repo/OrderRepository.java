package ibf2023.paf.day24.repo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ibf2023.paf.day24.model.LineItem;
import ibf2023.paf.day24.model.Order;

@Repository
public class OrderRepository implements Queries {

    @Autowired
    JdbcTemplate template;

    public void createLineItems(String orderId, List<LineItem> lineItems) {
        for (LineItem li: lineItems)
           template.update(SQL_LI_INSERT_LINEITEM, li.getItem(), li.getQuantity(), orderId);
    }

    public void createPurchaseOrder(String orderId, Order order) {
        template.update(SQL_PO_INSERT_PURCHASEORDER, orderId, order.getEmail(), order.getDeliveryDate()
            , order.isRush(), order.getComments());
    }

}
