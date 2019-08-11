package de.washeuteessen.management.importjob.mongo;

import de.washeuteessen.management.importjob.Job;
import de.washeuteessen.management.importjob.metrics.ImportMetrics;
import de.washeuteessen.management.recipe.Recipe;
import de.washeuteessen.management.recipe.RecipeRepository;
import de.washeuteessen.management.source.RecipeMongoSource;
import de.washeuteessen.management.source.RecipeMongoSourceRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Optional;

@AllArgsConstructor
public class MongoJob extends Job {

    private static final Logger LOGGER = LoggerFactory.getLogger(MongoJob.class);

    private RecipeMongoSourceRepository repository;

    @Override
    public void runAndSaveTo(final RecipeRepository recipeRepository, final ImportMetrics importMetrics) {

        final Query query = new Query();
        query.addCriteria(Criteria.where("imported").exists(false));
        query.limit(100);

        LOGGER.info("starting mongo import ...");

        this.repository.findNewToImport(PageRequest.of(0, 100))
                .map(this::markAsImported)
                .filter(RecipeMongoSource::isValid)
                .filter(RecipeMongoSource::hasImage)
                .map(RecipeMongoSource::toRecipe)
                .subscribe(
                        recipe -> {
                            final Optional<Recipe> existingRecipe = recipeRepository.getByUrl(recipe.getUrl());
                            if (existingRecipe.isPresent()) {
                                existingRecipe.get().updateWith(recipe);
                                recipeRepository.save(existingRecipe.get());
                                importMetrics.incrementUpdatedImports();
                            } else {
                                recipeRepository.save(recipe);
                                importMetrics.incrementNewImports();
                            }
                            importMetrics.incrementTotalImports();
                        },
                        error -> {
                            importMetrics.incrementFailedImports();
                            error.printStackTrace();
                        },
                        () -> LOGGER.info("mongo import done"));

    }

    private RecipeMongoSource markAsImported(final RecipeMongoSource recipeMongoSource) {

        recipeMongoSource.setImported(true);
        this.repository.save(recipeMongoSource);

        return recipeMongoSource;
    }

}
