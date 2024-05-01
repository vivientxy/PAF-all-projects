package paf.day4workshop.repo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import paf.day4workshop.Constants;
import paf.day4workshop.model.Order;

@Repository
public class MessageRepo implements Constants {
    
    @Autowired @Qualifier(REDIS_CONFIG_STRING)
    RedisTemplate<String,String> template;
    
    public void addName(String name) {
        ListOperations<String,String> listOps = template.opsForList();
        listOps.leftPush(REDIS_LIST_REGISTRATIONS, name);
    }

    public List<String> getNames() {
        ListOperations<String,String> listOps = template.opsForList();
        return listOps.range(REDIS_LIST_REGISTRATIONS, 0, -1);
    }

    public boolean containsName(String name) {
        ListOperations<String,String> listOps = template.opsForList();
        Long index = listOps.indexOf(REDIS_LIST_REGISTRATIONS, name);
        return index != null;
    }

    public void pushMessage(Order order) {
        String name = order.getCustomerName();
        ListOperations<String,String> listOps = template.opsForList();
        listOps.leftPush(name, order.toJson().toString());
    }
}
