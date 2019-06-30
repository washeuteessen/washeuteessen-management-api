package de.washeuteessen.management.importjob.mongo.repository;

import org.springframework.data.repository.reactive.RxJava2CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeMongoSourceRepository extends RxJava2CrudRepository<RecipeMongoSource, String> {
}
