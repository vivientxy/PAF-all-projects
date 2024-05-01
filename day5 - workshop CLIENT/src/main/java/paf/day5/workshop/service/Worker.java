package paf.day5.workshop.service;

import java.io.StringReader;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import paf.day5.workshop.Day5WorkshopApplication;
import paf.day5.workshop.model.Order;
import paf.day5.workshop.model.OrderDetail;

public class Worker implements Runnable {

    RedisTemplate<String, String> template;
    OrderService svc;

    public Worker(RedisTemplate<String, String> template, OrderService service) {
        this.template = template;
        this.svc = service;
    }

    @Override
    public void run() {
        System.out.println("*** Starting worker thread");
        ListOperations<String, String> listOps = template.opsForList();
        while (true)
            try {
                System.out.println("*** Queuing ");
                System.out.println("appname: " + Day5WorkshopApplication.appName);
                Optional<String> opt = Optional.ofNullable(listOps.rightPop(Day5WorkshopApplication.appName, Duration.ofSeconds(30)));

                if (opt.isEmpty())
                    continue;

                String payload = opt.get();
                JsonReader reader = Json.createReader(new StringReader(payload));
                JsonObject jsonObject = reader.readObject();

                Order order = new Order(jsonObject);
                JsonArray arr = jsonObject.getJsonArray("line_items");
                List<OrderDetail> details = arr.stream().map(j -> new OrderDetail(j)).toList();
                
                svc.addOrderAndDetails(order, details);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
    }
    
}
