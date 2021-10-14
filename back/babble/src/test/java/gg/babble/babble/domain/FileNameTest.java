package gg.babble.babble.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import gg.babble.babble.exception.BabbleIllegalArgumentException;
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

    @DisplayName("올바른 파일 이름 형식이 아닌 경우 예외처리")
    @Test
    void invlaidFileName() {
        assertThatThrownBy(() -> FileName.of("abc")).isInstanceOf(BabbleIllegalArgumentException.class);
    }
}
