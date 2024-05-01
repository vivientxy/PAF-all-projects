package paf.day4workshop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import paf.day4workshop.exception.OrderException;
import paf.day4workshop.model.Order;
import paf.day4workshop.model.OrderDetail;
import paf.day4workshop.repo.OrderRepo;

@Service
public class OrderService {
    
    @Autowired
    OrderRepo repo;

    @Transactional(rollbackFor = OrderException.class)
    public void addOrderAndDetails(Order order, List<OrderDetail> details) throws OrderException {
        try {
            int orderId = repo.addOrder(order);
            int detailId = 0;
            for (OrderDetail detail : details)
                detailId = repo.addOrderDetail(orderId, detail);
            if (orderId == 0 || detailId == 0)
                throw new OrderException();
        } catch (Exception e) {
            throw new OrderException("Unable to add order");
        }
    }
}
