package paf.practice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import paf.practice.repo.GameRepo;

@SpringBootApplication
public class PracticeApplication implements CommandLineRunner {

	@Autowired
	GameRepo repo;

	public static void main(String[] args) {
		SpringApplication.run(PracticeApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println(repo.aggregateGamesWithComments("Catan"));
	}

}
