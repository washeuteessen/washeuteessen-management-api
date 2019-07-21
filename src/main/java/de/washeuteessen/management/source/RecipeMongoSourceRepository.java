package de.washeuteessen.management.source;

import io.reactivex.Flowable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.reactive.RxJava2CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeMongoSourceRepository extends RxJava2CrudRepository<RecipeMongoSource, String> {

    @Query("{ 'imported': { $exists: false } }")
    Flowable<RecipeMongoSource> findNewToImport(Pageable pageable);

}
