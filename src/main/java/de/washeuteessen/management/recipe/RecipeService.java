package de.washeuteessen.management.recipe;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RecipeService {

    private static final Logger LOG = LoggerFactory.getLogger(RecipeService.class);

    private RecipeRepository recipeRepository;

}
