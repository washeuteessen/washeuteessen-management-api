package de.washeuteessen.management.importjob.mongo;

import de.washeuteessen.management.importjob.DuplicationFilter;
import de.washeuteessen.management.importjob.Job;
import de.washeuteessen.management.importjob.mongo.repository.RecipeMongoSource;
import de.washeuteessen.management.recipe.RecipeRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

@AllArgsConstructor
public class MongoJob extends Job {

    private static final Logger LOGGER = LoggerFactory.getLogger(MongoJob.class);

    private MongoOperations repository;

    @Override
    public void runAndSaveTo(final RecipeRepository recipeRepository) {

        final DuplicationFilter duplicationFilter = new DuplicationFilter(recipeRepository);

        final Query query = new Query();
        query.addCriteria(Criteria.where("imported").exists(false));
        query.limit(100);

        this.repository.find(query, RecipeMongoSource.class).stream()
                .map(this::markAsImported)
                .filter(RecipeMongoSource::isValid)
                .map(RecipeMongoSource::toRecipe)
                .map(duplicationFilter::updateExisting)
                .forEach(recipeRepository::save);

        LOGGER.info("mongo import done");
    }

    private RecipeMongoSource markAsImported(final RecipeMongoSource recipeMongoSource) {

        recipeMongoSource.setImported(true);
        this.repository.save(recipeMongoSource);

        return recipeMongoSource;
    }


}
