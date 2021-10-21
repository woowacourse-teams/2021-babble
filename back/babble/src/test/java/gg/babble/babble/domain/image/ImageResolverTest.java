package gg.babble.babble.domain.image;

import static org.assertj.core.api.Assertions.assertThat;

import gg.babble.babble.domain.FileName;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.imageio.ImageIO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ImageResolverTest {

    @DisplayName("이미지 리사이징")
    @ValueSource(strings = {"test.jpg", "test.png"})
    @ParameterizedTest
    void resizedImagesContaining(final String input) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(Objects.requireNonNull(classLoader.getResource("test-image.jpg")).getFile());
        ImageResolver imageResolver = new ImageResolver(new ImageFile(FileName.of(input), Files.readAllBytes(file.toPath())));

        List<ImageFile> imageFiles = imageResolver.resizedImagesContaining(Collections.singletonList(1920));
        assertThat(imageFiles).hasSize(1);
        assertThat(imageFiles.get(0).getName()).hasToString("test-x1920.jpg");

        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageFiles.get(0).getData());
            BufferedInputStream bufferedInputStream = new BufferedInputStream(byteArrayInputStream)) {
            BufferedImage bufferedImage = ImageIO.read(bufferedInputStream);

            assertThat(Math.max(bufferedImage.getWidth(), bufferedImage.getHeight())).isEqualTo(1920);
        }
    }
}
