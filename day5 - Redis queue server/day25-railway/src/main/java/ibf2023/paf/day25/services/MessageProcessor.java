package ibf2023.paf.day25.services;

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.time.Duration;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import ibf2023.paf.day25.models.MessageObject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@Component
//public class MessageProcessor implements MessageListener {
public class MessageProcessor {

	@Autowired @Qualifier("myredis")
	private RedisTemplate<String, String> template;

	@Async
	public void start() {
		Runnable poller = () -> {
			final ListOperations<String, String> listOps = template.opsForList();
			while (true) {
				try {
					System.out.println("\n*** POLLING ***");
					Optional<String> opt = Optional.ofNullable(
						listOps.rightPop("myqueue", Duration.ofSeconds(30)));
					if (opt.isPresent()) {
						System.out.printf("*** NEW MESSAGE: %s\n", opt.get());
						JsonReader reader = Json.createReader(new StringReader(opt.get()));
						JsonObject data = reader.readObject();
						MessageObject msgObj = MessageObject.toMessageObject(data);
						MessageObject result = new MessageObject(msgObj.id(), 
							"[%s]:%s".formatted((new Date()).toString(), msgObj.message().toUpperCase()));
						System.out.printf("**** PUBLISHING: %s\n", result.toString());
						template.convertAndSend("mytopic", result.toJson().toString());
					}
				} catch (Exception ex) {
					System.out.printf(">>> exception: %s\n", ex.getMessage());
				}
			}
		};

		System.out.println("*** Starting poller");
		Executors.newSingleThreadExecutor().execute(poller);
	}

	/*
	@Override
	public void onMessage(Message msg, byte[] pattern) {
		byte[] payload = msg.getBody();

		JsonReader reader = Json.createReader(new ByteArrayInputStream(payload));
		JsonObject data = reader.readObject();

		System.out.printf("*** %s\n", data.toString());
		MessageObject msgObj = MessageObject.toMessageObject(data);

	}
	*/
}
