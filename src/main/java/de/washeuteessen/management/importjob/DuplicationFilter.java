package de.washeuteessen.management.importjob;

import de.washeuteessen.management.importjob.metrics.ImportMetrics;
import de.washeuteessen.management.recipe.Recipe;
import de.washeuteessen.management.recipe.RecipeRepository;
import lombok.AllArgsConstructor;

import java.util.Optional;

@AllArgsConstructor
public class DuplicationFilter {

    private RecipeRepository recipeRepository;
    private ImportMetrics importMetrics;

    public Recipe updateExisting(final Recipe recipe) {
        final Optional<Recipe> existingRecipe = recipeRepository.getByUrl(recipe.getUrl());
        if (existingRecipe.isPresent()) {
            this.importMetrics.incrementUpdatedImports();
            return existingRecipe.get().updateWith(recipe);
        } else {
            this.importMetrics.incrementNewImports();
            return recipe;
        }
    }

}
