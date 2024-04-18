package ibf2023.paf.day25.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import ibf2023.paf.day25.Constant;
import jakarta.json.Json;
import jakarta.json.JsonObject;

@Component
public class MessageSender {

    @Autowired @Qualifier("myredis")
    private RedisTemplate<String, String> template;

    @Async
    public void sendMsg(String id, String message) {
        // craft JsonObject
        JsonObject jsonObject = Json.createObjectBuilder()
                .add("id", id)
                .add("message", message)
                .build();
        // push JsonObject into queue
        final ListOperations<String, String> listOps = template.opsForList();
        listOps.leftPush(Constant.REDIS_QUEUE, jsonObject.toString());
    }
}
