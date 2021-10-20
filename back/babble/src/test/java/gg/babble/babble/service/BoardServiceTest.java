package gg.babble.babble.service;

import static org.assertj.core.api.Assertions.assertThat;

import gg.babble.babble.ApplicationTest;
import gg.babble.babble.dto.request.board.BoardCreateRequest;
import gg.babble.babble.dto.response.BoardResponse;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class BoardServiceTest extends ApplicationTest {

    @Autowired
    private BoardService boardService;

    @DisplayName("게시글을 추가한다.")
    @Test
    void create() {
        //given
        BoardCreateRequest request = new BoardCreateRequest("테스트를 위해 작성된 게시글",
            "테스트가 과연 성공할 수 있을까요?\n이 테스트가 성공한다면 엄청난 일이 일어날지도...?",
            "자유",
            "테스터",
            "123456");

        //when
        BoardResponse response = boardService.create(request);
        BoardResponse expected = new BoardResponse(1L,
            "테스트를 위해 작성된 게시글",
            "테스트가 과연 성공할 수 있을까요?\n이 테스트가 성공한다면 엄청난 일이 일어날지도...?",
            "자유",
            "테스터",
            LocalDateTime.now().toString(),
            LocalDateTime.now().toString(),
            0,
            0);

        //then
        assertThat(response).usingRecursiveComparison().ignoringFields("id", "createdAt", "updatedAt").isEqualTo(expected);
    }

}
