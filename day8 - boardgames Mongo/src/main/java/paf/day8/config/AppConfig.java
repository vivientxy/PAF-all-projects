package paf.day8.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration
public class AppConfig {
    @Value("${spring.data.mongodb.uri}")
    private String mongoUrl;

    @Bean
    public MongoTemplate createMongoTemplate() {
        MongoClient client = MongoClients.create(mongoUrl);
        return new MongoTemplate(client, "boardgames");
    }

    @Bean
    public CommonsRequestLoggingFilter log() {
        CommonsRequestLoggingFilter logger = new CommonsRequestLoggingFilter();
        logger.setIncludeClientInfo(true);
        logger.setIncludeQueryString(true);
        return logger;
    }
}
