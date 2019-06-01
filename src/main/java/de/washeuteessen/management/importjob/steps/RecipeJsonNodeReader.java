package de.washeuteessen.management.importjob.steps;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import de.washeuteessen.management.importjob.model.RecipeJsonNode;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class RecipeJsonNodeReader implements ItemReader<RecipeJsonNode> {

    private JsonToken current;
    private JsonParser jp;
    private File file;

    @Value("#{jobParameters['file']}")
    public void setFileName(final String file) {
        this.file = new File(file);
    }

    private void initialize() {

        if (null == file) {
            throw new RuntimeException("file is null");
        }

        try {
            final JsonFactory f = new MappingJsonFactory();
            this.jp = f.createParser(new FileInputStream(file));

            this.current = jp.nextToken();
            if (this.current != JsonToken.START_ARRAY) {
                throw new RuntimeException("invalid json, should be an array");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public RecipeJsonNode read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {

        if (null == this.jp) {
            this.initialize();
        }

        if ((this.current = this.jp.nextToken()) != JsonToken.END_ARRAY) {
            final JsonNode jsonNode = jp.readValueAsTree();
            return new RecipeJsonNode(jsonNode);
        } else {
            return null;
        }

    }

}
