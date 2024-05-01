package paf.day5.workshop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import paf.day5.workshop.repo.MessageRepo;
import paf.day5.workshop.service.MessageProcessor;

@SpringBootApplication
public class Day5WorkshopApplication implements CommandLineRunner {

	@Autowired
	MessageRepo repo;

	@Autowired
	MessageProcessor processor;

	public static String appName = "";
	
	public static void main(String[] args) {
		SpringApplication.run(Day5WorkshopApplication.class, args);
	}
	@Override
	public void run(String... args) throws Exception {
		if (args.length > 0) {
			appName = args[0];
			if (!repo.containsName(appName))
				repo.addName(appName);
		}
		
		processor.start();
	}

}
