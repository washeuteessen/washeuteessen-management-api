package de.washeuteessen.management.importjob.model;

import com.fasterxml.jackson.databind.JsonNode;
import de.washeuteessen.management.recipe.Recipe;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RecipeJsonNode {

    private JsonNode jsonNode;

    public Recipe toRecipe() {
        final Recipe recipe = new Recipe();
        recipe.setTitle(this.jsonNode.get("title").asText());
        recipe.setUrl(this.jsonNode.get("url").asText());
        if (null != this.jsonNode.get("imgSrc")) {
            recipe.setImageSrc(this.jsonNode.get("imgSrc").asText());
        }
        //recipe.setText(this.jsonNode.get("text").asText());

        final IngridientsJsonNode ingridientsJsonNode = new IngridientsJsonNode(this.jsonNode.get("ingredients"));
        recipe.setIngredients(ingridientsJsonNode.getIngredients());

        return recipe;
    }

}
