package de.washeuteessen.management.importjob;

import de.washeuteessen.management.importjob.metrics.ImportMetrics;
import de.washeuteessen.management.recipe.RecipeRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ImportService {

    private RecipeRepository recipeRepository;
    private ImportMetrics importMetrics;

    @Async
    public void run(final Job job) {

        job.runAndSaveTo(this.recipeRepository, this.importMetrics);

    }


}
