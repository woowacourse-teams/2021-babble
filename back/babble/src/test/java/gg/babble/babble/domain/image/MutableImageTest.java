package gg.babble.babble.domain.image;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MutableImageTest {

    @DisplayName("이미지 리사이징")
    @Test
    void resize() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(Objects.requireNonNull(classLoader.getResource("test-image.jpg")).getFile());

        MutableImage mutableImage = MutableImage.of(Files.readAllBytes(file.toPath()));
        MutableImage actual = mutableImage.resize(new ImageSize(12, 10));

        assertThat(actual.getWidth()).isEqualTo(12);
        assertThat(actual.getHeight()).isEqualTo(10);
    }
}
