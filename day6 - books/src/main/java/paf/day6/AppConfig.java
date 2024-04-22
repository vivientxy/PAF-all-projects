package paf.day6;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration
public class AppConfig {

    @Value("${spring.data.mongodb.uri}")
    private String mongoUrl;

    // @Bean(Constants.SHOWS_DB)
    // public MongoTemplate createMongoTemplateTV() {
    //     MongoClient client = MongoClients.create(mongoUrl);
    //     return new MongoTemplate(client, Constants.SHOWS_DB);
    // }

    @Bean(Constants.LIBRARY_DB)
    public MongoTemplate createMongoTemplateBooks() {
        MongoClient client = MongoClients.create(mongoUrl);
        return new MongoTemplate(client, Constants.LIBRARY_DB);
    }
}
