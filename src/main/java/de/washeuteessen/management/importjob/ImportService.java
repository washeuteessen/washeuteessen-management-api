package de.washeuteessen.management.importjob;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import de.washeuteessen.management.importjob.model.RecipeJsonNode;
import de.washeuteessen.management.recipe.RecipeRepository;
import io.reactivex.Emitter;
import io.reactivex.Flowable;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
@AllArgsConstructor
public class ImportService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImportService.class);

    private RecipeRepository recipeRepository;

    @Async
    public void run(final File file) throws IOException {

        final JsonFactory f = new MappingJsonFactory();
        final JsonParser jp = f.createParser(new FileInputStream(file));

        JsonToken current = jp.nextToken();
        if (current != JsonToken.START_ARRAY) {
            throw new RuntimeException("invalid json, should be an array");
        }

        long time1 = new Date().getTime();
        final Flowable<RecipeJsonNode> flowable = Flowable.generate(
                () -> jp,
                this::pullOrComplete,
                JsonParser::close
        );

        flowable.map(RecipeJsonNode::toRecipe)
                .buffer(1, TimeUnit.SECONDS)
                .subscribe(
                        recipes -> this.recipeRepository.saveAll(recipes),
                        error -> LOGGER.error("import failed", error),
                        () -> LOGGER.info("import done"));

    }

    private void pullOrComplete(JsonParser parser, Emitter<RecipeJsonNode> emitter) throws IOException {
        if (parser.nextToken() != JsonToken.END_ARRAY) {
            final JsonNode jsonNode = parser.readValueAsTree();
            emitter.onNext(new RecipeJsonNode(jsonNode));
        } else {
            emitter.onComplete();
        }
    }


}
