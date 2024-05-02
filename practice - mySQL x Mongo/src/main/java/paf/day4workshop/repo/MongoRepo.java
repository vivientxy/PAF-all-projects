package paf.day4workshop.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import paf.day4workshop.model.Order;

@Repository
public class MongoRepo {

    @Autowired
    private MongoTemplate template;
    
    // save the Order object (complete with id and OrderDetails) into Mongo
    public Order saveOrder(Order order) {
        return template.save(order, "orders");
    }

}
