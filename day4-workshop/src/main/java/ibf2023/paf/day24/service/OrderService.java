package ibf2023.paf.day24.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ibf2023.paf.day24.model.Order;
import ibf2023.paf.day24.repo.OrderRepository;

@Service
public class OrderService {

   @Autowired
   OrderRepository repo;


   @Transactional(rollbackFor = OrderException.class)
   public void insertPurchaseOrder(Order po) 
         // rollback
         throws OrderException {
 
      // start transaction
      try {
         String poId = UUID.randomUUID().toString().substring(0, 8);
         repo.createPurchaseOrder(poId, po);
         repo.createLineItems(poId, po.getLineItems());
      } catch (Exception ex) {
         throw new OrderException(ex.getMessage());
      }
      // commit
   }
    
}
