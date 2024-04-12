package paf.day1.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import paf.day1.exception.CustomerNotFoundException;
import paf.day1.model.Customer;
import paf.day1.model.Order;
import paf.day1.service.CustomerService;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {
    
    @Autowired
    CustomerService customerService;

    @GetMapping
    public List<Customer> getAllCustomers(@RequestParam("limit") int limit, @RequestParam("offset") int offset) {
        return customerService.getAllCustomersWithPagination(limit, offset);
    }

    @GetMapping("/{id}")
    public Customer getCustomerById(@PathVariable("id") int id) throws CustomerNotFoundException {
        return customerService.getCustomerById(id);
    }

    @GetMapping("/{id}/orders")
    public List<Order> getCustomerOrders(@PathVariable("id") int id) throws CustomerNotFoundException {
        return customerService.getCustomerByOrders(id);
    }
    
}
