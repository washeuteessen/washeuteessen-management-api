package de.washeuteessen.management.recipe;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String url;
    private String imageSrc;
    private String source;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "INGREDIENTS",
            joinColumns = @JoinColumn(name = "RECIPE_ID")
    )
    @Column(name = "INGREDIENT")
    private List<String> ingredients;

    public Recipe updateWith(Recipe recipe) {
        this.title = recipe.getTitle();
        this.imageSrc = recipe.getImageSrc();
        this.url = recipe.getUrl();
        this.imageSrc = recipe.getImageSrc();
        this.source = recipe.getSource();
        this.ingredients.clear();
        this.ingredients.addAll(recipe.getIngredients());
        return this;
    }
}
