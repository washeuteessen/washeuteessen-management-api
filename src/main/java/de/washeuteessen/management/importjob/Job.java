package de.washeuteessen.management.importjob;

import de.washeuteessen.management.importjob.metrics.ImportMetrics;
import de.washeuteessen.management.recipe.RecipeRepository;

public abstract class Job {

    public abstract void runAndSaveTo(RecipeRepository recipeRepository, final ImportMetrics importMetrics);

}
