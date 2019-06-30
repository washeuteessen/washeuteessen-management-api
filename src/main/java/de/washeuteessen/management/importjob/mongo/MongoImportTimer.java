package de.washeuteessen.management.importjob.mongo;

import de.washeuteessen.management.importjob.ImportService;
import de.washeuteessen.management.importjob.Job;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MongoImportTimer {

    private MongoOperations mongoOperations;
    private ImportService importService;

    @Scheduled(cron = "${washeuteessen.management.mongoImportCron}")
    public void runDbJob() {

        final Job job = new MongoJob(this.mongoOperations);

        this.importService.run(job);
    }

}
