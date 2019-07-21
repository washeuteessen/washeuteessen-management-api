package de.washeuteessen.management.importjob;

import de.washeuteessen.management.importjob.mongo.MongoJob;
import de.washeuteessen.management.source.RecipeMongoSourceRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MongoImportTimer {

    private RecipeMongoSourceRepository recipeMongoSourceRepository;
    private ImportService importService;

    @Scheduled(cron = "${washeuteessen.management.mongoImportCron}")
    public void runDbJob() {

        final Job job = new MongoJob(this.recipeMongoSourceRepository);

        this.importService.run(job);
    }

}
