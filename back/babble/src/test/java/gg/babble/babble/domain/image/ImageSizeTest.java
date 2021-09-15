package gg.babble.babble.domain.image;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ImageSizeTest {

    @DisplayName("해당 픽셀 수를 최대 길이로 가지는 사이즈 계산")
    @Test
    void calculateSizeContaining() {
        ImageSize imageSize = new ImageSize(10, 12);

        assertThat(imageSize.calculateSizeContaining(120)).isEqualTo(new ImageSize(100, 120));
    }
}
