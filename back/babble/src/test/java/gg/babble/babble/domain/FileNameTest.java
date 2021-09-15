package gg.babble.babble.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FileNameTest {

    @DisplayName("파일 이름 파싱 테스트")
    @Test
    void of() {
        FileName fileName = FileName.of("abc.txt.jpg");
        assertThat(fileName.getSimpleName()).isEqualTo("abc.txt");
        assertThat(fileName.getExtension()).isEqualTo("jpg");
    }
}
