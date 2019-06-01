package de.washeuteessen.management.importjob;

import de.washeuteessen.management.recipe.RecipeRepository;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@AllArgsConstructor
public class ImportService {

    private RecipeRepository recipeRepository;
    private Job job;
    private JobLauncher jobLauncher;

    @Async
    public void run(final File file) {

        try {
            final JobParameters parameters = new JobParametersBuilder()
                    .addString("file", file.getPath())
                    .toJobParameters();

            this.jobLauncher.run(this.job, parameters);

        } catch (JobExecutionAlreadyRunningException e) {
            e.printStackTrace();
        } catch (JobRestartException e) {
            e.printStackTrace();
        } catch (JobInstanceAlreadyCompleteException e) {
            e.printStackTrace();
        } catch (JobParametersInvalidException e) {
            e.printStackTrace();
        }
    }


}
