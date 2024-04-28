package paf.day8;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import paf.day8.repo.GameRepo;
import paf.day8.service.GameService;

@SpringBootApplication
public class Day8Application implements CommandLineRunner {

	@Autowired
	GameRepo repo;

	@Autowired
	GameService svc;

	public static void main(String[] args) {
		SpringApplication.run(Day8Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// System.out.println(">>>>>> " + svc.isGameIdExist(150));
	}

}
