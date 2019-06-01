package de.washeuteessen.management.importjob.steps;

import de.washeuteessen.management.importjob.model.RecipeJsonNode;
import de.washeuteessen.management.recipe.Recipe;
import org.springframework.batch.item.ItemProcessor;

public class RecipeProcessor implements ItemProcessor<RecipeJsonNode, Recipe> {

    @Override
    public Recipe process(RecipeJsonNode recipeJsonNode) throws Exception {
        return recipeJsonNode.toRecipe();
    }

}
