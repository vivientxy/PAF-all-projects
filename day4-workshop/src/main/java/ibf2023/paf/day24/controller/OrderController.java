package ibf2023.paf.day24.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ibf2023.paf.day24.service.OrderService;

@RestController
@RequestMapping("/api")
public class OrderController {

    @Autowired
    OrderService svc;

    
}
