package paf.day8;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import paf.day8.repo.GameRepo;

@SpringBootApplication
public class Day8Application implements CommandLineRunner {

	@Autowired
	GameRepo repo;

	public static void main(String[] args) {
		SpringApplication.run(Day8Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		for (Document doc : repo.getGamesWithComments("ticket")) {
			System.out.println(">>>>>> " + doc);
		}
	}

}
