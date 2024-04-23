package paf.day27;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import paf.day27.model.Person;
import paf.day27.repo.PersonRepo;

@SpringBootApplication
public class Day27Application implements CommandLineRunner {

	@Autowired
	PersonRepo repo;

	public static void main(String[] args) {
		SpringApplication.run(Day27Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Person person1 = new Person("Bobby", 20, "M", Arrays.asList("eating","sleeping","swimming"));
		repo.savePerson(person1);

		Person person2 = new Person("Andy", 25, "M", Arrays.asList("skating","surfing","partying"));
		repo.savePerson(person2);

		Person person3 = new Person("Chelsea", 18, "F", Arrays.asList("origami","baking","skiing"));
		repo.savePerson(person3);
	}

}
