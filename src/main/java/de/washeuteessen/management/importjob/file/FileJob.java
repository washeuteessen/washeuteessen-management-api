package de.washeuteessen.management.importjob.file;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import de.washeuteessen.management.importjob.DuplicationFilter;
import de.washeuteessen.management.importjob.Job;
import de.washeuteessen.management.importjob.file.model.RecipeJsonNode;
import de.washeuteessen.management.importjob.metrics.ImportMetrics;
import de.washeuteessen.management.recipe.Recipe;
import de.washeuteessen.management.recipe.RecipeRepository;
import io.reactivex.Emitter;
import io.reactivex.Flowable;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@AllArgsConstructor
public class FileJob extends Job {

    private final static Logger LOGGER = LoggerFactory.getLogger(FileJob.class);

    private File file;

    @Override
    public void runAndSaveTo(final RecipeRepository recipeRepository, final ImportMetrics importMetrics) {
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

            final DuplicationFilter duplicationFilter = new DuplicationFilter(recipeRepository, importMetrics);
            flowable.map(RecipeJsonNode::toRecipe)
                    .filter(this::isValid)
                    .filter(this::hasImage)
                    .map(duplicationFilter::updateExisting)
                    .subscribe(
                            recipeRepository::save,
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


    private boolean isValid(Recipe recipe) {
        if (StringUtils.isEmpty(recipe.getUrl())) {
            return false;
        } else if (StringUtils.isEmpty(recipe.getSource())) {
            return false;
        } else if (StringUtils.isEmpty(recipe.getTitle())) {
            return false;
        } else {
            return true;
        }
    }

    private boolean hasImage(Recipe recipe) {
        return !StringUtils.isEmpty(recipe.getImageSrc());
    }

}
