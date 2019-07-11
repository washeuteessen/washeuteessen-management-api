package de.washeuteessen.management.importjob.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import org.springframework.stereotype.Service;

@Service
public class ImportMetrics {

    private final Counter total_imports;
    private final Counter new_imports;
    private final Counter updated_imports;
    private final Counter failed_imports;

    public ImportMetrics() {
        this.total_imports = Metrics.counter("imports.total");
        this.new_imports = Metrics.counter("imports.new");
        this.updated_imports = Metrics.counter("imports.updated");
        this.failed_imports = Metrics.counter("imports.failed");
    }

    public ImportMetrics incrementTotalImports() {
        this.total_imports.increment();
        return this;
    }

    public ImportMetrics incrementNewImports() {
        this.new_imports.increment();
        return this;
    }

    public ImportMetrics incrementUpdatedImports() {
        this.updated_imports.increment();
        return this;
    }

    public ImportMetrics incrementFailedImports() {
        this.failed_imports.increment();
        return this;
    }

}
