package de.washeuteessen.management.importjob.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
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
        ((ArrayNode) ingridientsJsonNode).forEach(jsonNode -> {
            this.ingredients.add(jsonNode.get("name").textValue());
        });

    }

}
