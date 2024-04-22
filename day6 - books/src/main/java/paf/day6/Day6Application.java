package paf.day6;

// import java.util.List;

// import org.bson.Document;
// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// import paf.day6.repo.TVShowRepo;

@SpringBootApplication
public class Day6Application implements CommandLineRunner {

	// @Autowired
	// private TVShowRepo repo;

	public static void main(String[] args) {
		SpringApplication.run(Day6Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		// for (Document doc : repo.findShowsByName("and")) {
		// 	String name = doc.getString("name");
		// 	List<String> genres = doc.getList("genres", String.class);
		// 	System.out.println(">>> name: " + name + ", genres: " + genres);
		// 	System.out.println(">>> doc: " + doc.toJson());
		// }

		// Long count = repo.countShowsByLanguage("english");
		// System.out.println(">>> count: " + count);

		// System.out.println("Type of shows with an average rating of gte 7: " + repo.getTypesByRating(7.0f));

		// System.exit(0);
	}

}
