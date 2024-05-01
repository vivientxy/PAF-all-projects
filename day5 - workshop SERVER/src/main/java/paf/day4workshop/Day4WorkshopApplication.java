package paf.day4workshop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import paf.day4workshop.repo.MessageRepo;

@SpringBootApplication
public class Day4WorkshopApplication implements CommandLineRunner {

	@Autowired
	MessageRepo repo;

	public static void main(String[] args) {
		SpringApplication.run(Day4WorkshopApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		// ONLY APPLICABLE FOR CLIENT:
		
		// if (args.length > 0) {
		// 	String appName = args[0];
		// 	System.out.println(">>> does repo contain name?: " + repo.containsName(appName));
		// 	if (!repo.containsName(appName)) {
		// 		System.out.println("...adding name...");
		// 		repo.addName(appName);
		// 	}
		// }

	}

}
