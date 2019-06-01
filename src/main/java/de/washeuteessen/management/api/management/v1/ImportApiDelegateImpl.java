package de.washeuteessen.management.api.management.v1;

import de.washeuteessen.management.api.swagger.v1.ImportApiDelegate;
import de.washeuteessen.management.importjob.ImportService;
import lombok.AllArgsConstructor;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Component
@AllArgsConstructor
public class ImportApiDelegateImpl implements ImportApiDelegate {

    private ImportService importService;

    @Override
    public ResponseEntity<Void> startImport(MultipartFile file) {

        try {
            final File tempFile = this.createTempFile(file.getInputStream());
            this.importService.run(tempFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok().build();
    }

    private File createTempFile(final InputStream inputStream) throws IOException {
        final File file = File.createTempFile("import_", "");
        try (final OutputStream outputStream = new FileOutputStream(file)) {
            IOUtils.copy(inputStream, outputStream);
        }
        return file;
    }
}
