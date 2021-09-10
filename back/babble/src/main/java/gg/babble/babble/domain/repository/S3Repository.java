package gg.babble.babble.domain.repository;

import java.util.Set;

public interface S3Repository {

    Set<String> findAllImages();

    void save(final String fileName, final byte[] content);
}
