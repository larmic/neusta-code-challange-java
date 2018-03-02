package de.neusta.ncc.infrastructure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * Simple helper component to send an upload request for a given csv file in resources/upload folder.
 */
@Component
public class UploadRequestSender {

    @Autowired
    private TestRestTemplate testRestTemplate;

    public <T> ResponseEntity<T> sendUploadRequest(String file, Class<T> responseType) {
        return sendUploadRequest(file, HttpMethod.POST, responseType);
    }

    public <T> ResponseEntity<T> sendUploadRequest(String file, HttpMethod httpMethod, Class<T> responseType) {
        final FileSystemResource fileSystemResource = buildFileResource(file);
        final HttpEntity requestEntity = buildHttpEntity(fileSystemResource);

        return testRestTemplate.exchange("/api/import", httpMethod, requestEntity, responseType);
    }

    private FileSystemResource buildFileResource(String file) {
        return file != null ? new FileSystemResource("src/test/resources/upload/" + file) : null;
    }

    private HttpEntity buildHttpEntity(final FileSystemResource fileSystemResource) {
        final MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();
        parameters.add("file", fileSystemResource);

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        return new HttpEntity<>(parameters, headers);
    }

}
