package ibf2023.paf.day25.services;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class MessageReader {
	
	@Autowired @Qualifier("myredis")
	private RedisTemplate<String, String> template;

	@Async // create a thread
	public void start() {
		ExecutorService threadPool = Executors.newFixedThreadPool(2); // 2 counters opened
		threadPool.submit(new MessageReaderWorker(template)); // worker 1
		threadPool.submit(new MessageReaderWorker(template)); // worker 2
	}
}
