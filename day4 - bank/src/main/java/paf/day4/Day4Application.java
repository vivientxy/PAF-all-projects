package paf.day4;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import paf.day4.service.AccountsException;
import paf.day4.service.AccountsService;

@SpringBootApplication
public class Day4Application implements CommandLineRunner {

	@Autowired
	AccountsService svc;

	public static void main(String[] args) {
		SpringApplication.run(Day4Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		try {
			svc.fundsTransfer("1234", "abcd", 200);
		} catch (AccountsException e) {
			System.out.println(">>> error: " + e.getMessage());
		}

		System.exit(0);

	}

}
