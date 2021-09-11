package gg.babble.babble.domain.repository.s3repository;

import gg.babble.babble.domain.repository.S3Repository;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
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
}
