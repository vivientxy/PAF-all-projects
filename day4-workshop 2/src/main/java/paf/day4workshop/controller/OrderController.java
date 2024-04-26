package paf.day4workshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import paf.day4workshop.model.Order;

@Controller
public class OrderController {

    @PostMapping("/order")
    public String processOrder(@ModelAttribute Order order) {
        


        return "order";
    }

}
