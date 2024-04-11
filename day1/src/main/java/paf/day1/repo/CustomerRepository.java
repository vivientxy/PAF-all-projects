package paf.day1.repo;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import paf.day1.model.Customer;
import paf.day1.model.Order;

@Repository
public class CustomerRepository implements Queries {
    
    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<Customer> getAllCustomers() {
        List<Customer> result = new LinkedList<>();
        final SqlRowSet rs = jdbcTemplate.queryForRowSet(GET_ALL_CUSTOMERS);
        while (rs.next()) {
            Customer c = new Customer();
            c.setId(rs.getInt("id"));
            c.setFirstName(rs.getString("first_name"));
            c.setLastName(rs.getString("last_name"));
            result.add(c);
        }
        return Collections.unmodifiableList(result);
    }

    public List<Customer> getAllCustomersWithPagination(int limit, int offset) {
        List<Customer> result = new LinkedList<>();
        final SqlRowSet rs = jdbcTemplate.queryForRowSet(GET_ALL_CUSTOMERS_WITH_PAGINATION, limit, offset);
        while (rs.next()) {
            Customer c = new Customer();
            c.setId(rs.getInt("id"));
            c.setFirstName(rs.getString("first_name"));
            c.setLastName(rs.getString("last_name"));
            result.add(c);
        }
        return Collections.unmodifiableList(result);
    }

    public Customer getCustomerById(int id) {
        Customer c = new Customer();
        final SqlRowSet rs = jdbcTemplate.queryForRowSet(GET_CUSTOMER_BY_ID, id);
        if (rs.next()) {
            c.setId(rs.getInt("id"));
            c.setFirstName(rs.getString("first_name"));
            c.setLastName(rs.getString("last_name"));
        }
        return c;
    }

    public boolean isCustomerExist(int id){
        boolean isExist = false;
        final SqlRowSet rs = jdbcTemplate.queryForRowSet(IS_CUSTOMER_EXIST, id);
        if (rs.next()) {
            int x = rs.getInt("count");
            if (x>0)
                isExist = true;
        }
        return isExist;
    }

    public List<Order> getCustomerByOrders(int id) {
        List<Order> result = new LinkedList<>();
        final SqlRowSet rs = jdbcTemplate.queryForRowSet(GET_CUSTOMER_BY_ORDER, id);
        while (rs.next()) {
            Order o = new Order();
            o.setId(rs.getInt("id"));
            o.setFirstName(rs.getString("first_name"));
            o.setLastName(rs.getString("last_name"));
            o.setTaxRate(rs.getDouble("tax_rate"));
            LocalDateTime time = (LocalDateTime) rs.getObject("order_date");
            o.setOrderDate(time);
            result.add(o);
        }
        return Collections.unmodifiableList(result);
    }

}
