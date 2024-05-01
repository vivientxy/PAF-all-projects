package paf.day4workshop.controller;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import paf.day4workshop.exception.OrderException;
import paf.day4workshop.model.Order;
import paf.day4workshop.model.OrderDetail;
import paf.day4workshop.service.OrderService;

@Controller
public class OrderController {

    @Autowired
    OrderService svc;

    @PostMapping("/order")
    public String processOrder(@ModelAttribute Order order, @RequestBody MultiValueMap<String,String> mvm) throws OrderException {
        List<OrderDetail> details = getOrderDetails(mvm);
        svc.addOrderAndDetails(order, details);
        return "order";
    }

    public List<OrderDetail> getOrderDetails(MultiValueMap<String,String> mvm) {
        List<OrderDetail> details = new LinkedList<>();
        if (!mvm.containsKey("product"))
            return details;

        for (int i = 0; i < mvm.get("product").size(); i++) {
            OrderDetail detail = new OrderDetail();
            detail.setProduct(mvm.get("product").get(i));
            detail.setUnitPrice(Float.parseFloat(mvm.get("unitPrice").get(i)));
            detail.setDiscount(Float.parseFloat(mvm.get("discount").get(i)));
            detail.setQuantity(Integer.parseInt(mvm.get("quantity").get(i)));
            details.add(detail);
        }
        return details;
    }

}