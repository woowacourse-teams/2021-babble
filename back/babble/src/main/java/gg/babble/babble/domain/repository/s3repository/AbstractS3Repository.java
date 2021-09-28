package gg.babble.babble.domain.repository.s3repository;

import gg.babble.babble.domain.repository.S3Repository;
import java.net.URLConnection;

public abstract class AbstractS3Repository implements S3Repository {

    private static final String IMAGE_CONTENT_TYPE = "image";

    protected boolean isImageFile(final String fileName) {
        final String contentType = URLConnection.guessContentTypeFromName(fileName);
        return contentType.contains(IMAGE_CONTENT_TYPE);
    }
}
