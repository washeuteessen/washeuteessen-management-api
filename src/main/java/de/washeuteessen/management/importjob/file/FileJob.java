package de.washeuteessen.management.importjob.file;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import de.washeuteessen.management.importjob.Job;
import de.washeuteessen.management.importjob.file.model.RecipeJsonNode;
import de.washeuteessen.management.recipe.RecipeRepository;
import io.reactivex.Emitter;
import io.reactivex.Flowable;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@AllArgsConstructor
public class FileJob extends Job {

    private final static Logger LOGGER = LoggerFactory.getLogger(FileJob.class);

    private File file;

    @Override
    public void runAndSaveTo(final RecipeRepository recipeRepository) {
        try {

            final JsonFactory f = new MappingJsonFactory();
            final JsonParser jp = f.createParser(new FileInputStream(this.file));

            JsonToken current = jp.nextToken();
            if (current != JsonToken.START_ARRAY) {
                throw new RuntimeException("invalid json, should be an array");
            }

            final Flowable<RecipeJsonNode> flowable = Flowable.generate(
                    () -> jp,
                    this::pullOrComplete,
                    JsonParser::close
            );

            flowable.map(RecipeJsonNode::toRecipe)
                    .buffer(1, TimeUnit.SECONDS)
                    .subscribe(
                            recipeRepository::saveAll,
                            error -> LOGGER.error("file import failed", error),
                            () -> LOGGER.info("file import done"));
        } catch (IOException e) {
            throw new RuntimeException("file job failed", e);
        }
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
