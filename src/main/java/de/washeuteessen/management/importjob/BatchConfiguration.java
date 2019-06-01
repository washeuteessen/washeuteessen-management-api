package de.washeuteessen.management.importjob;


import de.washeuteessen.management.importjob.model.RecipeJsonNode;
import de.washeuteessen.management.importjob.steps.RecipeJsonNodeReader;
import de.washeuteessen.management.importjob.steps.RecipeProcessor;
import de.washeuteessen.management.recipe.Recipe;
import de.washeuteessen.management.recipe.RecipeRepository;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
@AllArgsConstructor
public class BatchConfiguration {

    private JobBuilderFactory jobBuilderFactory;
    private StepBuilderFactory stepBuilderFactory;
    private RecipeRepository recipeRepository;

    @Bean
    @StepScope
    public RecipeJsonNodeReader reader() {
        return new RecipeJsonNodeReader();
    }

    @Bean
    public RecipeProcessor processor() {
        return new RecipeProcessor();
    }

    @Bean
    public RepositoryItemWriter<Recipe> writer() {
        final RepositoryItemWriter<Recipe> writer = new RepositoryItemWriter<>();
        writer.setRepository(this.recipeRepository);
        writer.setMethodName("save");
        return writer;
    }

    @Bean
    public Job importRecipeJob(Step step1) {
        return jobBuilderFactory.get("importRecipeJob")
                .incrementer(new RunIdIncrementer())
                .flow(step1)
                .end()
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<RecipeJsonNode, Recipe>chunk(10)
                .reader(this.reader())
                .processor(this.processor())
                .writer(this.writer())
                .build();
    }
}
