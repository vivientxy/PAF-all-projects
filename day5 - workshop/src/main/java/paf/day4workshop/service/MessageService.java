package paf.day4workshop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import paf.day4workshop.Constants;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Service
public class MessageService implements Constants {

    public String appName = "";
    
    @Autowired @Qualifier(REDIS_CONFIG_STRING)
    RedisTemplate<String,String> template;

    public void addNameToRedisList(String appName) {
        this.appName = appName;
		ListOperations<String,String> listOps = template.opsForList();
		listOps.leftPush(Constants.REDIS_LIST_REGISTRATIONS, appName);
	}

    public List<String> getSubscribers() {
        ListOperations<String,String> listOps = template.opsForList();
        return listOps.range(REDIS_LIST_REGISTRATIONS, 0, -1);
    }


}
