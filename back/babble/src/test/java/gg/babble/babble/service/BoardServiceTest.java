package gg.babble.babble.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import gg.babble.babble.ApplicationTest;
import gg.babble.babble.domain.board.Board;
import gg.babble.babble.dto.request.board.BoardCreateRequest;
import gg.babble.babble.dto.request.board.BoardDeleteRequest;
import gg.babble.babble.dto.request.board.BoardUpdateRequest;
import gg.babble.babble.dto.response.BoardResponse;
import gg.babble.babble.dto.response.BoardSearchResponse;
import gg.babble.babble.exception.BabbleIllegalArgumentException;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class BoardServiceTest extends ApplicationTest {

    @Autowired
    private BoardService boardService;

    private Board post1;
    private Board post2;
    private Board post3;
    private Board post4;
    private Board post5;
    private Board post6;

    @BeforeEach
    public void setUp() {
        post1 = boardRepository.save(new Board("이것은 처음쓴 글",
            "안녕하세요~!!!\n저는 첫 번째 글의 주인공이랍니다.",
            "자유",
            "조용히좋아요를눌러라",
            "123456"));
        post2 = boardRepository.save(new Board("나랑 밥먹을 사람?",
            "하이루 방가링가\n오늘은 중식이 땡기는구만\n어디 좋은 중국집 아는데 있는사람 추천좀 ㅎㅎ",
            "건의",
            "멘보샤",
            "234567"));
        post3 = boardRepository.save(new Board("니들이 게맛을 알아?",
            "간장게장 먹고싶다.\n누가 나 좀 간장게장좀 사줘~",
            "자유",
            "밥도둑",
            "345678"));
        post4 = boardRepository.save(new Board("게임 좀 추가해줘요.",
            "",
            "게임",
            "게임이좋아",
            "123456"));
        post5 = boardRepository.save(new Board("간장게장보단 양념게장이지~",
            "간장게장은 비린맛 때매 호불호 갈림 ㅋㅋ\n우리의 킹념개장을 찬양하라!!",
            "자유",
            "멘보샤",
            "234567"));
        post6 = boardRepository.save(new Board("게임할 사람 구함",
            "오늘은 날씨가 좋네요.\n저랑 같이 게임할 사람?",
            "게임",
            "킹장게장",
            "123123"));
    }

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
            false,
            0,
            0);

        //then
        assertThat(response).usingRecursiveComparison().ignoringFields("id", "createdAt", "updatedAt").isEqualTo(expected);
    }

    @DisplayName("게시글을 수정한다.")
    @Test
    void update() {
        //given
        BoardResponse expected = new BoardResponse(post1.getId(),
            "이것은 수정된 글",
            "수정된 글이지롱~",
            "건의",
            "조용히좋아요를눌러라",
            post1.createdAt(),
            post1.updatedAt(),
            post1.isNotice(),
            0,
            0);
        BoardUpdateRequest request = new BoardUpdateRequest(post1.getId(), "이것은 수정된 글",
            "수정된 글이지롱~",
            "건의",
            "123456");

        //when
        BoardResponse response = boardService.update(request);

        //then
        assertThat(response).usingRecursiveComparison().ignoringFields("updatedAt").isEqualTo(expected);
    }

    @DisplayName("잘못된 비밀번호로 수정 요청을 하면 예외가 발생한다.")
    @Test
    void updateWithWrongPassword() {
        BoardResponse expected = new BoardResponse(post1.getId(),
            "이것은 수정된 글",
            "수정된 글이지롱~",
            "건의",
            "조용히좋아요를눌러라",
            post1.createdAt(),
            post1.updatedAt(),
            post1.isNotice(),
            0,
            0);
        BoardUpdateRequest request = new BoardUpdateRequest(post1.getId(), "이것은 수정된 글",
            "수정된 글이지롱~",
            "건의",
            "1234567");

        //then
        assertThatThrownBy(() -> boardService.update(request)).isInstanceOf(BabbleIllegalArgumentException.class);
    }

    @DisplayName("조회를 하면 view 가 증가한다.")
    @Test
    void viewCount() {
        //when
        boardService.findById(post1.getId());
        boardService.findById(post1.getId());
        boardService.findById(post1.getId());
        boardService.findById(post1.getId());
        BoardResponse response = boardService.findById(post1.getId());

        //then
        assertThat(response.getView()).isEqualTo(5L);
    }

    @DisplayName("좋아요를 하면 like 가 증가한다.")
    @Test
    void likeCount() {
        //when
        boardService.like(post1.getId());
        boardService.like(post1.getId());
        BoardResponse response = boardService.like(post1.getId());

        //then
        assertThat(response.getLike()).isEqualTo(3L);
    }

    @DisplayName("원하는 제목로 검색을 한다.")
    @Test
    void findByTitle() {
        //when
        BoardSearchResponse boardSearchResponse = boardService.search("title", "간장게장");

        //then
        List<BoardResponse> results = boardSearchResponse.getResults();
        assertThat(results).hasSize(1);
        BoardResponse response = results.get(0);

        assertThat(response).usingRecursiveComparison().isEqualTo(BoardResponse.from(post5));
    }

    @DisplayName("원하는 내용으로 검색을 한다.")
    @Test
    void findByContent() {
        //when
        BoardSearchResponse response = boardService.search("titleAndContent", "간장게장");

        //then
        assertThat(response.getResults()).hasSize(2);
    }

    @DisplayName("원하는 작성자로 검색을 한다.")
    @Test
    void findByAuthor() {
        //when
        BoardSearchResponse response = boardService.search("author", "멘보샤");

        //then
        assertThat(response.getResults()).hasSize(2);
    }

    @DisplayName("제목 + 내용 + 작성자를 모두 포함한 단어로 검색을 한다.")
    @Test
    void findByAll() {
        //when
        BoardSearchResponse response = boardService.search("all", "게장");

        //then
        assertThat(response.getResults()).hasSize(3);
    }

    @DisplayName("카테고리로 검색을 한다.")
    @Test
    void findByCategory() {
        //when
        List<BoardResponse> responses = boardService.findByCategory("자유");

        //then
        assertThat(responses).hasSize(3);
    }

    @DisplayName("게시글을 조회한다.")
    @Test
    void findById() {
        //when
        BoardResponse response = boardService.findById(post4.getId());

        //then
        assertThat(response).usingRecursiveComparison().isEqualTo(BoardResponse.from(post4));
    }

    @DisplayName("전체 게시글을 조회한다.")
    @Test
    void findAll() {
        //when
        List<BoardResponse> responses = boardService.findAll();

        //then
        assertThat(responses).hasSize(6);
    }

    @DisplayName("게시글을 삭제한다.")
    @Test
    void delete() {
        //given
        BoardDeleteRequest request = new BoardDeleteRequest(post2.getId(), "234567");

        //when
        boardService.delete(request);

        //then
        List<BoardResponse> responses = boardService.findAll();
        assertThat(responses).hasSize(5);
    }

    @DisplayName("잘못된 비밀번호로 삭제 요청을 하면 예외가 발생한다.")
    @Test
    void deleteWithWrongPassword() {
        //given
        BoardDeleteRequest request = new BoardDeleteRequest(post2.getId(), "1234567");

        //then
        assertThatThrownBy(() -> boardService.delete(request)).isInstanceOf(BabbleIllegalArgumentException.class);
    }
}
