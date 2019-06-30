package de.washeuteessen.management.importjob.mongo.repository;

import de.washeuteessen.management.recipe.Recipe;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "recipes")
public class RecipeMongoSource {

    private ObjectId _id;
    private String title;
    private String domain;
    private String img_src;
    private List<String> ingredients;
    private String url;
    private String text;

    public Recipe toRecipe() {
        return Recipe.builder()
                .title(this.title)
                .source(this.domain)
                .imageSrc(this.img_src)
                .ingredients(this.ingredients)
                .url(this.url)
                .build();
    }
}
