package ibf2023.paf.day24.model;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Order {
    
    private String id;
    private String name;
    private String email;
    private Date deliveryDate;
    private boolean rush;
    private String comments;
    private Date lastUpdate;
    private List<LineItem> lineItems = new LinkedList<>();
 
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
 
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
 
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
 
    public Date getDeliveryDate() { return deliveryDate; }
    public void setDeliveryDate(Date deliveryDate) { this.deliveryDate = deliveryDate; }
 
    public boolean isRush() { return rush; }
    public void setRush(boolean rush) { this.rush = rush; }
 
    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }
 
    public List<LineItem> getLineItems() { return lineItems; }
    public void setLineItems(List<LineItem> lineItems) { this.lineItems = lineItems; }
    public void addLineItem(LineItem lineItem) { this.lineItems.add(lineItem); }
 
    public Date getLastUpdate() { return lastUpdate; }
    public void setLastUpdate(Date lastUpdate) { this.lastUpdate = lastUpdate; }
 
    @Override
    public String toString() {
       return "PurchaseOrder [id=" + id + ", name=" + name + ", email=" + email + ", deliveryDate=" + deliveryDate
             + ", rush=" + rush + ", comments=" + comments + ", lastUpdate=" + lastUpdate + "]";
    } 

}
