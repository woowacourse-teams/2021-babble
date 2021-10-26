package gg.babble.babble.domain.post;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import gg.babble.babble.exception.BabbleIllegalArgumentException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PostTest {

    @DisplayName("게시글이 8000bytes보다 크면 예외 처리")
    @Test
    void createPostWithInvalidBytesSize() {
        String sentence = "a";
        StringBuilder content = new StringBuilder();

        while (content.toString().getBytes().length <= 8000) {
            content.append(sentence);
        }

        assertThatThrownBy(() -> new Post("title", content.toString(), "자유", "nickname", "password"))
            .isExactlyInstanceOf(BabbleIllegalArgumentException.class);
    }

    @DisplayName("게시글 제목이 50글자보다 크면 예외 처리")
    @Test
    void createPostWithInvalidTitleLength() {
        String sentence = "a";
        StringBuilder title = new StringBuilder();

        for (int i = 0; i < 51; i++) {
            title.append(sentence);
        }

        assertThatThrownBy(() -> new Post(title.toString(), "content", "자유", "nickname", "password"))
            .isExactlyInstanceOf(BabbleIllegalArgumentException.class);;
    }
}
