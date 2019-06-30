package de.washeuteessen.management.importjob.mongo;

import de.washeuteessen.management.importjob.DuplicationFilter;
import de.washeuteessen.management.importjob.Job;
import de.washeuteessen.management.importjob.mongo.repository.RecipeMongoSource;
import de.washeuteessen.management.recipe.RecipeRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoOperations;

@AllArgsConstructor
public class MongoJob extends Job {

    private static final Logger LOGGER = LoggerFactory.getLogger(MongoJob.class);

    private MongoOperations repository;

    @Override
    public void runAndSaveTo(final RecipeRepository recipeRepository) {

        final DuplicationFilter duplicationFilter = new DuplicationFilter(recipeRepository);
        this.repository.findAll(RecipeMongoSource.class).stream()
                .map(RecipeMongoSource::toRecipe)
                .map(duplicationFilter::updateExisting)
                .forEach(recipeRepository::save);

        LOGGER.info("mongo import done");
    }


}
