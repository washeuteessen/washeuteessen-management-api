package de.washeuteessen.management.importjob.file.model;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class IngridientsJsonNode {

    private List<String> ingredients;

    public IngridientsJsonNode(final JsonNode ingridientsJsonNode) {

        if (!ingridientsJsonNode.isArray()) {
            throw new RuntimeException("can not instantiate ingredients of object");
        }

        this.ingredients = new ArrayList<>();
        /**((ArrayNode) ingridientsJsonNode).forEach(jsonNode -> {
         this.ingredients.add(jsonNode.get("name").textValue());
         });**/

        this.ingredients.add(ingridientsJsonNode.asText(null));
    }

}
