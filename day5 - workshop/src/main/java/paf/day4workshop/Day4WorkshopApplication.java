package paf.day4workshop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import paf.day4workshop.service.MessageService;
import paf.day4workshop.service.MessageSubscriber;

@SpringBootApplication
public class Day4WorkshopApplication {

	@Autowired
	MessageSubscriber sub;

	@Autowired
	static
	MessageService msgSvc;

	public static void main(String[] args) {
		SpringApplication.run(Day4WorkshopApplication.class, args);

		if (args.length != 0) {
			String appName = args[0];
			msgSvc.addNameToRedisList(appName);
		}
	}

}
