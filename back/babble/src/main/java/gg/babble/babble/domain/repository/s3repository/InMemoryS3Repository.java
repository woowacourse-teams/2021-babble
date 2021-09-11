package gg.babble.babble.domain.repository.s3repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Profile("local")
@Repository
public class InMemoryS3Repository extends AbstractS3Repository {

    private final Map<String, byte[]> files = new HashMap<>();

    @Override
    public Set<String> findAllImages() {
        return files.keySet()
            .stream()
            .filter(this::isImageFile)
            .collect(Collectors.toSet());
    }

    @Override
    public void save(final String fileName, final byte[] content) {
        files.put(fileName, content);
    }

    @Override
    public void delete(final String... fileNames) {
        for (String fileName : fileNames) {
            files.remove(fileName);
        }
    }
}
