package paf.day5.workshop.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import paf.day5.workshop.utils.Constants;

@Service
public class MessageProcessor implements Constants {

    @Autowired
    OrderService svc;

    @Autowired @Qualifier(REDIS_CONFIG_STRING)
    private RedisTemplate<String,String> template;

    @Async
    public void start() {
        ExecutorService threadPool = Executors.newFixedThreadPool(2);
        threadPool.submit(new Worker(template, svc));
    }
    
}
