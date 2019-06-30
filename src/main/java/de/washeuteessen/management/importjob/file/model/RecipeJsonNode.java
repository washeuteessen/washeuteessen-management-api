package de.washeuteessen.management.importjob.file.model;

import com.fasterxml.jackson.databind.JsonNode;
import de.washeuteessen.management.recipe.Recipe;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RecipeJsonNode {

    private JsonNode jsonNode;

    public Recipe toRecipe() {
        final Recipe recipe = new Recipe();
        recipe.setTitle(this.jsonNode.get("title").asText(null));
        recipe.setUrl(this.jsonNode.get("url").asText(null));
        if (null != this.jsonNode.get("img_src")) {
            recipe.setImageSrc(this.jsonNode.get("img_src").asText(null));
        }
        recipe.setSource(this.jsonNode.get("domain").asText(null));
        //recipe.setText(this.jsonNode.get("text").asText());

        final IngridientsJsonNode ingridientsJsonNode = new IngridientsJsonNode(this.jsonNode.get("ingredients"));
        recipe.setIngredients(ingridientsJsonNode.getIngredients());

        return recipe;
    }

}
