package gg.babble.babble.domain.repository;

import java.util.List;

public interface S3Repository {

    List<String> findAllImages();

    void save(final String fileName, final byte[] content);

    void delete(final String... fileNames);
}
