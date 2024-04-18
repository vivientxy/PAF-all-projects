package ibf2023.paf.day25;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import ibf2023.paf.day25.services.MessageProcessor;

@SpringBootApplication
@EnableAsync
public class Day25Application implements CommandLineRunner {

	@Autowired
	private MessageProcessor processor;

	public static void main(String[] args) {
		SpringApplication.run(Day25Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		processor.start();
	}

}
