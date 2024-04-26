package paf.day4workshop.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import paf.day4workshop.model.Order;

@Repository
public class OrderRepo implements Queries {
    
    @Autowired
    JdbcTemplate template;

    public Boolean addOrder(Order o) {
        //INSERT INTO orders(order_date,customer_name,ship_address,notes,tax)
        // VALUES (?,?,?,?,?)
        template.update(SQL_ADD_ORDER, o.getOrderDate(), o.getCustomerName(), o.getShipAddress(), o.getNotes(), o.getTax());

        return true;
    }

}
