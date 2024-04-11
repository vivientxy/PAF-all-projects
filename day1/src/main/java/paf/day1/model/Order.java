package paf.day1.model;

import java.time.LocalDateTime;

public class Order extends Customer {
    
    private double taxRate;
    private LocalDateTime orderDate;

    public Order() {
    }
    public double getTaxRate() {
        return taxRate;
    }
    public void setTaxRate(double taxRate) {
        this.taxRate = taxRate;
    }
    public LocalDateTime getOrderDate() {
        return orderDate;
    }
    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

}
