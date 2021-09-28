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
import org.junit.jupiter.api.Test;

class ImageResolverTest {

    private static final String IMAGE_FILE_NAME = "test.jpg";

    @DisplayName("이미지 리사이징")
    @Test
    void resizedImagesContaining() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(Objects.requireNonNull(classLoader.getResource("test-image.jpg")).getFile());
        ImageResolver imageResolver = new ImageResolver(new ImageFile(FileName.of(IMAGE_FILE_NAME), Files.readAllBytes(file.toPath())));

        List<ImageFile> imageFiles = imageResolver.resizedImagesContaining(Collections.singletonList(10));
        assertThat(imageFiles).hasSize(1);
        assertThat(imageFiles.get(0).getName()).hasToString("test-x10.jpg");

        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageFiles.get(0).getData());
            BufferedInputStream bufferedInputStream = new BufferedInputStream(byteArrayInputStream)) {
            BufferedImage bufferedImage = ImageIO.read(bufferedInputStream);

            assertThat(Math.max(bufferedImage.getWidth(), bufferedImage.getHeight())).isEqualTo(10);
        }
    }
}
