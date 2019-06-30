package de.washeuteessen.management.importjob.mongo;

import de.washeuteessen.management.importjob.Job;
import de.washeuteessen.management.importjob.mongo.repository.RecipeMongoSource;
import de.washeuteessen.management.recipe.Recipe;
import de.washeuteessen.management.recipe.RecipeRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Optional;

@AllArgsConstructor
public class MongoJob extends Job {

    private static final Logger LOGGER = LoggerFactory.getLogger(MongoJob.class);

    private MongoOperations repository;

    @Override
    public void runAndSaveTo(final RecipeRepository recipeRepository) {

        System.out.println(this.repository.count(new Query(), RecipeMongoSource.class));

        this.repository.findAll(RecipeMongoSource.class).stream()
                .map(RecipeMongoSource::toRecipe)
                .map(recipe -> {
                    final Optional<Recipe> existingRecipie = recipeRepository.getByUrl(recipe.getUrl());
                    if (existingRecipie.isPresent()) {
                        return existingRecipie.get().updateWith(recipe);
                    } else {
                        return recipe;
                    }
                })
                .forEach(recipeRepository::save);

        LOGGER.info("mongo import done");
    }


}
