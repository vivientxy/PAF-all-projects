package paf.day5.workshop.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import paf.day5.workshop.model.Order;
import paf.day5.workshop.model.OrderDetail;

@Repository
public class OrderRepo implements Queries {
    
    @Autowired
    JdbcTemplate template;

    public int addOrder(Order o) {
        // INSERT INTO orders(order_date,customer_name,ship_address,notes,tax)
        // VALUES (?,?,?,?,?)
        template.update(SQL_ADD_ORDER, o.getOrderDate(), o.getCustomerName(), o.getShipAddress(), o.getNotes(), o.getTax());
        return template.queryForObject(SQL_GET_ID, Integer.class);
    }

    public int addOrderDetail(int orderId, OrderDetail d) {
        // INSERT INTO orders_details(product,unit_price,discount,quantity,order_id)
        // VALUES (?,?,?,?,?)
        template.update(SQL_ADD_ORDER_DETAIL, d.getProduct(), d.getUnitPrice(), d.getDiscount(), d.getQuantity(), orderId);;
        return template.queryForObject(SQL_GET_ID, Integer.class);
    }

}