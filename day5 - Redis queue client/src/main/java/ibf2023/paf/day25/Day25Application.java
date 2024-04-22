package ibf2023.paf.day25;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import ibf2023.paf.day25.services.MessagePublisher;
import ibf2023.paf.day25.services.MessageReader;
import ibf2023.paf.day25.services.MessageSender;

@SpringBootApplication
@EnableAsync
public class Day25Application implements CommandLineRunner {

	@Autowired
	private MessageSender sender;

	@Autowired
	private MessageReader reader;

	@Autowired
	private MessagePublisher publisher;

	public static void main(String[] args) {
		SpringApplication.run(Day25Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// to send a msg
		Scanner scanner = new Scanner(System.in);
		boolean stop = false;
		while (!stop) {
            // Read a message from the console
			// Thread.sleep(10000); // add delay
			System.out.println();
			// System.out.print("Input your id: ");
			String id = scanner.nextLine();
			// Check if the user wants to exit
			if ("exit".equalsIgnoreCase(id.trim()))
				stop = true;
			System.out.print("Input your message: ");
			String message = scanner.nextLine();
            sender.sendMsg(id, message);
		}
		scanner.close();

		publisher.start(); // read incoming msg, transform and publish the msg (single threadpool)
		reader.start(); // to read incoming messages (2 workers in threadpool)
	}
}