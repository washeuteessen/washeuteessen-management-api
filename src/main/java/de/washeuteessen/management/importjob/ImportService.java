package de.washeuteessen.management.importjob;

import de.washeuteessen.management.recipe.RecipeRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ImportService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImportService.class);

    private RecipeRepository recipeRepository;

    @Async
    public void run(final Job job) {

        job.runAndSaveTo(this.recipeRepository);

    }


}
