package paf.day4workshop.service;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import paf.day4workshop.exception.OrderException;
import paf.day4workshop.model.Order;
import paf.day4workshop.model.OrderDetail;
import paf.day4workshop.repo.MongoRepo;
import paf.day4workshop.repo.OrderRepo;

@Service
public class OrderService {
    
    @Autowired
    OrderRepo repo;

    @Autowired
    MongoRepo mongoRepo;

    @Transactional(rollbackFor = OrderException.class)
    public Order addOrderAndDetailsToSQL(Order order, List<OrderDetail> details) throws OrderException {
        try {
            int orderId = repo.addOrder(order);
            int detailId = 0;

            List<OrderDetail> newList = new LinkedList<>();

            for (OrderDetail detail : details) {
                detailId = repo.addOrderDetail(orderId, detail);
                detail.setId(detailId);
                detail.setOrderId(orderId);
                newList.add(detail);
            }

            if (orderId == 0 || detailId == 0) // check SQL injection for both Order and OrderDetails succeeded?
                throw new OrderException();

            order.setOrderId(orderId);
            order.setOrderDetails(newList);
            return order;
        } catch (Exception e) {
            throw new OrderException("Unable to add order");
        }
    }

    public Order addOrderMongo(Order order) {
        return mongoRepo.saveOrder(order);
    }
}
