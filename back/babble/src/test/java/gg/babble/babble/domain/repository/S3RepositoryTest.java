package gg.babble.babble.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Set;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class S3RepositoryTest {

    private static final String IMAGE_FILE_NAME = "test.jpg";
    @Autowired
    private S3Repository s3Repository;

    @BeforeEach
    void setUp() throws IOException {
        final ClassLoader classLoader = getClass().getClassLoader();
        final File file = new File(Objects.requireNonNull(classLoader.getResource("test-image.jpg")).getFile());

        deleteAllImageFile();

        s3Repository.save(IMAGE_FILE_NAME, Files.readAllBytes(Paths.get(file.getAbsolutePath())));
        s3Repository.save("textFile.txt", "abc".getBytes(StandardCharsets.UTF_8));
    }

    @AfterEach
    void afterAll() {
        deleteAllImageFile();
    }

    private void deleteAllImageFile() {
        final Set<String> allImages = s3Repository.findAllImages();

        for (String image : allImages) {
            s3Repository.delete(image);
        }
    }

    @DisplayName("이미지 저장 확인 테스트")
    @Test
    void findAllImages() {
        final Set<String> allImages = s3Repository.findAllImages();
        assertThat(allImages).hasSize(1);
        assertThat(allImages).contains(IMAGE_FILE_NAME);
    }
}
