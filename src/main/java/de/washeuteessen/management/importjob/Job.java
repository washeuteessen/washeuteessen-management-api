package de.washeuteessen.management.importjob;

import de.washeuteessen.management.recipe.RecipeRepository;

public abstract class Job {

    public abstract void runAndSaveTo(RecipeRepository recipeRepository);

}
