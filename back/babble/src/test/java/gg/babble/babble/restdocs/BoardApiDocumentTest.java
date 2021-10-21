package gg.babble.babble.restdocs;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import gg.babble.babble.dto.response.BoardResponse;
import gg.babble.babble.dto.response.BoardSearchResponse;
import io.restassured.response.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class BoardApiDocumentTest extends AcceptanceTest {

    private List<BoardResponse> boardResponses;

    @BeforeEach
    public void setUp() {
        boardResponses = new ArrayList<>();

        boardResponses.add(게시글_추가("이것은 처음쓴 글",
            "안녕하세요~!!!\n저는 첫 번째 글의 주인공이랍니다.",
            "자유",
            "조용히좋아요를눌러라",
            "123456"));
        boardResponses.add(게시글_추가("나랑 밥먹을 사람?",
            "하이루 방가링가\n오늘은 중식이 땡기는구만\n어디 좋은 중국집 아는데 있는사람 추천좀 ㅎㅎ",
            "건의",
            "멘보샤",
            "234567"));
        boardResponses.add(게시글_추가("니들이 게맛을 알아?",
            "간장게장 먹고싶다.\n누가 나 좀 간장게장좀 사줘~",
            "자유",
            "밥도둑",
            "345678"));
        boardResponses.add(게시글_추가("게임 좀 추가해줘요.",
            "",
            "게임",
            "게임이좋아",
            "123456"));
        boardResponses.add(게시글_추가("간장게장보단 양념게장이지~",
            "간장게장은 비린맛 때매 호불호 갈림 ㅋㅋ\n우리의 킹념개장을 찬양하라!!",
            "자유",
            "멘보샤",
            "234567"));
        boardResponses.add(게시글_추가("게임할 사람 구함",
            "오늘은 날씨가 좋네요.\n저랑 같이 게임할 사람?",
            "게임",
            "킹장게장",
            "123123"));
    }

    private BoardResponse 게시글_추가(final String title, final String content, final String category, final String nickname, final String password) {
        Map<String, Object> body = new HashMap<>();
        body.put("title", title);
        body.put("content", content);
        body.put("category", category);
        body.put("nickname", nickname);
        body.put("password", password);

        return given().body(body)
            .when().post("/api/board")
            .then().statusCode(HttpStatus.CREATED.value())
            .extract().body().as(BoardResponse.class);
    }

    @DisplayName("게시글을 추가한다.")
    @Test
    void createBoard() {
        BoardResponse board = boardResponses.get(0);

        Map<String, Object> body = new HashMap<>();
        body.put("title", board.getTitle());
        body.put("content", board.getContent());
        body.put("category", board.getCategory());
        body.put("nickname", board.getNickname());
        body.put("password", "123456");

        BoardResponse response = given().body(body)
            .filter(document("create-board",
                requestFields(fieldWithPath("title").description("제목"),
                    fieldWithPath("content").description("내용"),
                    fieldWithPath("category").description("카테고리"),
                    fieldWithPath("nickname").description("닉네임"),
                    fieldWithPath("password").description("비밀번호")),
                responseFields(fieldWithPath("id").description("게시글 Id"),
                    fieldWithPath("id").description("게시글 Id"),
                    fieldWithPath("title").description("제목"),
                    fieldWithPath("content").description("내용"),
                    fieldWithPath("category").description("카테고리"),
                    fieldWithPath("nickname").description("닉네임"),
                    fieldWithPath("createdAt").description("생성 일자"),
                    fieldWithPath("updatedAt").description("최근 수정 일자"),
                    fieldWithPath("notice").description("공지사항"),
                    fieldWithPath("view").description("조회수"),
                    fieldWithPath("like").description("좋아요"))))
            .when().post("/api/board")
            .then().statusCode(HttpStatus.CREATED.value())
            .extract().body().as(BoardResponse.class);

        assertThat(response).usingRecursiveComparison().ignoringFields("id", "createdAt", "updatedAt").isEqualTo(board);
    }

    @DisplayName("게시글을 단일 조회한다.")
    @Test
    void findBoard() {
        BoardResponse board = boardResponses.get(1);

        BoardResponse response = given()
            .filter(document("read-board",
                responseFields(fieldWithPath("id").description("게시글 Id"),
                    fieldWithPath("title").description("제목"),
                    fieldWithPath("content").description("내용"),
                    fieldWithPath("category").description("카테고리"),
                    fieldWithPath("nickname").description("닉네임"),
                    fieldWithPath("createdAt").description("생성 일자"),
                    fieldWithPath("updatedAt").description("최근 수정 일자"),
                    fieldWithPath("notice").description("공지사항"),
                    fieldWithPath("view").description("조회수"),
                    fieldWithPath("like").description("좋아요"))))
            .when().get("/api/board/{id}", boardResponses.get(1).getId())
            .then().statusCode(HttpStatus.OK.value())
            .extract().body().as(BoardResponse.class);

        assertThat(response).usingRecursiveComparison().ignoringFields("id", "createdAt", "updatedAt").isEqualTo(board);
    }

    @DisplayName("게시글을 전체 조회한다.")
    @Test
    void findAllBoard() {
        List<BoardResponse> responses = given()
            .filter(document("read-boards",
                responseFields(fieldWithPath("[].id").description("게시글 Id"),
                    fieldWithPath("[].title").description("제목"),
                    fieldWithPath("[].content").description("내용"),
                    fieldWithPath("[].category").description("카테고리"),
                    fieldWithPath("[].nickname").description("닉네임"),
                    fieldWithPath("[].createdAt").description("생성 일자"),
                    fieldWithPath("[].updatedAt").description("최근 수정 일자"),
                    fieldWithPath("[].notice").description("공지사항"),
                    fieldWithPath("[].view").description("조회수"),
                    fieldWithPath("[].like").description("좋아요"))))
            .when().get("/api/board")
            .then().statusCode(HttpStatus.OK.value())
            .extract().body().jsonPath().getList(".", BoardResponse.class);

        assertThat(responses).hasSize(6);
    }

    @DisplayName("게시글을 검색한다.")
    @Test
    void searchBoard() {
        Map<String, Object> body = new HashMap<>();
        body.put("keyword", "게장");
        body.put("type", "titleAndContent");

        BoardSearchResponse response = given().body(body)
            .filter(document("search-boards",
                requestFields(fieldWithPath("keyword").description("검색어"),
                    fieldWithPath("type").description("검색 타입")),
                responseFields(fieldWithPath("results.[].id").description("게시글 Id"),
                    fieldWithPath("results.[].title").description("제목"),
                    fieldWithPath("results.[].content").description("내용"),
                    fieldWithPath("results.[].category").description("카테고리"),
                    fieldWithPath("results.[].nickname").description("닉네임"),
                    fieldWithPath("results.[].createdAt").description("생성 일자"),
                    fieldWithPath("results.[].updatedAt").description("최근 수정 일자"),
                    fieldWithPath("results.[].notice").description("공지사항"),
                    fieldWithPath("results.[].view").description("조회수"),
                    fieldWithPath("results.[].like").description("좋아요"),
                    fieldWithPath("keyword").description("검색어"),
                    fieldWithPath("type").description("검색 타입"))))
            .when().get("/api/board/search")
            .then().statusCode(HttpStatus.OK.value())
            .extract().body().as(BoardSearchResponse.class);

        assertThat(response.getResults()).hasSize(2);
    }

    @DisplayName("게시글을 카테고리로 검색한다.")
    @Test
    void searchBoardByCategory() {
        Map<String, Object> body = new HashMap<>();
        body.put("category", "자유");

        List<BoardResponse> responses = given().body(body)
            .filter(document("category-boards",
                requestFields(fieldWithPath("category").description("카테고리")),
                responseFields(fieldWithPath("[].id").description("게시글 Id"),
                    fieldWithPath("[].title").description("제목"),
                    fieldWithPath("[].content").description("내용"),
                    fieldWithPath("[].category").description("카테고리"),
                    fieldWithPath("[].nickname").description("닉네임"),
                    fieldWithPath("[].createdAt").description("생성 일자"),
                    fieldWithPath("[].updatedAt").description("최근 수정 일자"),
                    fieldWithPath("[].notice").description("공지사항"),
                    fieldWithPath("[].view").description("조회수"),
                    fieldWithPath("[].like").description("좋아요"))))
            .when().get("/api/board/category")
            .then().statusCode(HttpStatus.OK.value())
            .extract().body().jsonPath().getList(".", BoardResponse.class);

        assertThat(responses).hasSize(3);
    }

    @DisplayName("게시글을 수정한다.")
    @Test
    void updateBoard() {
        BoardResponse board = boardResponses.get(2);

        Map<String, Object> body = new HashMap<>();
        body.put("id", board.getId());
        body.put("title", "니들은 게맛을 몰라~");
        body.put("content", "게장은 다 좋은 것이여~\n게장 만들기 게임도 추가해주세요. ㅎㅎ");
        body.put("category", "건의");
        body.put("password", "345678");

        BoardResponse response = given().body(body)
            .filter(document("update-board",
                requestFields(fieldWithPath("id").description("검색어"),
                    fieldWithPath("title").description("제목"),
                    fieldWithPath("content").description("내용"),
                    fieldWithPath("category").description("카테고리"),
                    fieldWithPath("password").description("비밀번호")),
                responseFields(fieldWithPath("id").description("게시글 Id"),
                    fieldWithPath("id").description("게시글 Id"),
                    fieldWithPath("title").description("제목"),
                    fieldWithPath("content").description("내용"),
                    fieldWithPath("category").description("카테고리"),
                    fieldWithPath("nickname").description("닉네임"),
                    fieldWithPath("createdAt").description("생성 일자"),
                    fieldWithPath("updatedAt").description("최근 수정 일자"),
                    fieldWithPath("notice").description("공지사항"),
                    fieldWithPath("view").description("조회수"),
                    fieldWithPath("like").description("좋아요"))))
            .when().put("/api/board")
            .then().statusCode(HttpStatus.OK.value())
            .extract().body().as(BoardResponse.class);

        assertThat(response.getId()).isEqualTo(board.getId());
        assertThat(response.getTitle()).isEqualTo("니들은 게맛을 몰라~");
        assertThat(response.getContent()).isEqualTo("게장은 다 좋은 것이여~\n게장 만들기 게임도 추가해주세요. ㅎㅎ");
        assertThat(response.getCategory()).isEqualTo("건의");
        assertThat(response.getCreatedAt()).isEqualTo(board.getCreatedAt());
    }

    @DisplayName("게시글을 삭제한다.")
    @Test
    void deleteBoard() {
        BoardResponse board = boardResponses.get(3);

        Map<String, Object> body = new HashMap<>();
        body.put("id", board.getId());
        body.put("password", "123456");

        given().body(body)
            .filter(document("delete-board",
                requestFields(fieldWithPath("id").description("검색어"),
                    fieldWithPath("password").description("비밀번호"))))
            .when().delete("/api/board")
            .then().statusCode(HttpStatus.NO_CONTENT.value());

        Response response = 게시글_조회(board.getId());
        response.then().statusCode(HttpStatus.NOT_FOUND.value());
    }

    private Response 게시글_조회(final Long id) {
        return given()
            .when().get("/api/board/{id}", id);
    }
}
