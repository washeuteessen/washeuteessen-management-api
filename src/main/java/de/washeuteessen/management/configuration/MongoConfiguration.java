package de.washeuteessen.management.configuration;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories({"de.washeuteessen.management.importjob.mongo.repository"})
public class MongoConfiguration extends AbstractMongoConfiguration {

    private String mongoDbUri;
    private String database;

    public MongoConfiguration(
            @Value("${spring.data.mongodb.uri}") String mongoDbUri,
            @Value("${spring.data.mongodb.database}") String database) {
        this.mongoDbUri = mongoDbUri;
        this.database = database;
    }

    @Override
    protected String getDatabaseName() {
        return this.database;
    }

    @Override
    public MongoClient mongoClient() {
        return new MongoClient(new MongoClientURI(this.mongoDbUri));
    }

}
