package de.washeuteessen.management.configuration;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@Configuration
@EnableReactiveMongoRepositories({"de.washeuteessen.management.source"})
public class MongoConfiguration extends AbstractReactiveMongoConfiguration {

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
    public MongoClient reactiveMongoClient() {
        return MongoClients.create(this.mongoDbUri);
    }
}
