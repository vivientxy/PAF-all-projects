package paf.day1.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import paf.day1.exception.CustomerNotFoundException;
import paf.day1.model.Customer;
import paf.day1.model.Order;
import paf.day1.repo.CustomerRepository;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public List<Customer> getAllCustomers() {
        return customerRepository.getAllCustomers();
    }

    public List<Customer> getAllCustomersWithPagination(int limit, int offset) {
        return customerRepository.getAllCustomersWithPagination(limit, offset);
    }

    public Customer getCustomerById(int id) {
        return customerRepository.getCustomerById(id);
    }

    public List<Order> getCustomerByOrders(int id) throws CustomerNotFoundException{
        boolean customerExist = customerRepository.isCustomerExist(id);
        if(!customerExist){
            throw new CustomerNotFoundException("Customer with id: "+id+" not found");
        }
        return customerRepository.getCustomerByOrders(id);
    }

}
