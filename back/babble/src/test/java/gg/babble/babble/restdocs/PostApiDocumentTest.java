package gg.babble.babble.restdocs;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import gg.babble.babble.dto.response.PostResponse;
import gg.babble.babble.dto.response.PostSearchResponse;
import io.restassured.response.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class PostApiDocumentTest extends AcceptanceTest {

    private List<PostResponse> postResponses;

    @BeforeEach
    public void setUp() {
        postResponses = new ArrayList<>();

        postResponses.add(게시글_추가("이것은 처음쓴 글",
            "안녕하세요~!!!\n저는 첫 번째 글의 주인공이랍니다.",
            "자유",
            "조용히좋아요를눌러라",
            "123456"));
        postResponses.add(게시글_추가("나랑 밥먹을 사람?",
            "하이루 방가링가\n오늘은 중식이 땡기는구만\n어디 좋은 중국집 아는데 있는사람 추천좀 ㅎㅎ",
            "건의",
            "멘보샤",
            "234567"));
        postResponses.add(게시글_추가("니들이 게맛을 알아?",
            "간장게장 먹고싶다.\n누가 나 좀 간장게장좀 사줘~",
            "자유",
            "밥도둑",
            "345678"));
        postResponses.add(게시글_추가("게임 좀 추가해줘요.",
            "",
            "게임",
            "게임이좋아",
            "123456"));
        postResponses.add(게시글_추가("간장게장보단 양념게장이지~",
            "간장게장은 비린맛 때매 호불호 갈림 ㅋㅋ\n우리의 킹념개장을 찬양하라!!",
            "자유",
            "멘보샤",
            "234567"));
        postResponses.add(게시글_추가("게임할 사람 구함",
            "오늘은 날씨가 좋네요.\n저랑 같이 게임할 사람?",
            "게임",
            "킹장게장",
            "123123"));
    }

    private PostResponse 게시글_추가(final String title, final String content, final String category, final String nickname, final String password) {
        Map<String, Object> body = new HashMap<>();
        body.put("title", title);
        body.put("content", content);
        body.put("category", category);
        body.put("nickname", nickname);
        body.put("password", password);

        return given().body(body)
            .when().post("/api/post")
            .then().statusCode(HttpStatus.CREATED.value())
            .extract().body().as(PostResponse.class);
    }

    @DisplayName("게시글을 추가한다.")
    @Test
    void createPost() {
        PostResponse post = postResponses.get(0);

        Map<String, Object> body = new HashMap<>();
        body.put("title", post.getTitle());
        body.put("content", post.getContent());
        body.put("category", post.getCategory());
        body.put("nickname", post.getNickname());
        body.put("password", "123456");

        PostResponse response = given().body(body)
            .filter(document("create-post",
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
            .when().post("/api/post")
            .then().statusCode(HttpStatus.CREATED.value())
            .extract().body().as(PostResponse.class);

        assertThat(response).usingRecursiveComparison().ignoringFields("id", "createdAt", "updatedAt").isEqualTo(post);
    }

    @DisplayName("게시글을 단일 조회한다.")
    @Test
    void findPost() {
        PostResponse post = postResponses.get(1);

        PostResponse response = given()
            .filter(document("read-post",
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
            .when().get("/api/post/{id}", postResponses.get(1).getId())
            .then().statusCode(HttpStatus.OK.value())
            .extract().body().as(PostResponse.class);

        assertThat(response).usingRecursiveComparison().ignoringFields("id", "createdAt", "updatedAt", "view").isEqualTo(post);
        assertThat(response.getView()).isEqualTo(1L);
    }

    @DisplayName("게시글을 전체 조회한다.")
    @Test
    void findAllPost() {
        List<PostResponse> responses = given()
            .filter(document("read-posts",
                responseFields(fieldWithPath("[].id").description("게시글 Id"),
                    fieldWithPath("[].title").description("제목"),
                    fieldWithPath("[].category").description("카테고리"),
                    fieldWithPath("[].nickname").description("닉네임"),
                    fieldWithPath("[].createdAt").description("생성 일자"),
                    fieldWithPath("[].updatedAt").description("최근 수정 일자"),
                    fieldWithPath("[].notice").description("공지사항"),
                    fieldWithPath("[].view").description("조회수"),
                    fieldWithPath("[].like").description("좋아요"))))
            .when().get("/api/post?page=1")
            .then().statusCode(HttpStatus.OK.value())
            .extract().body().jsonPath().getList(".", PostResponse.class);

        assertThat(responses).hasSize(6);
    }

    @DisplayName("게시글을 페이지 단위로 조회한다.")
    @Test
    void findAllPostWithPagination() {
        List<PostResponse> responses = given()
            .filter(document("read-posts-page",
                responseFields(fieldWithPath("[].id").description("게시글 Id"),
                    fieldWithPath("[].title").description("제목"),
                    fieldWithPath("[].category").description("카테고리"),
                    fieldWithPath("[].nickname").description("닉네임"),
                    fieldWithPath("[].createdAt").description("생성 일자"),
                    fieldWithPath("[].updatedAt").description("최근 수정 일자"),
                    fieldWithPath("[].notice").description("공지사항"),
                    fieldWithPath("[].view").description("조회수"),
                    fieldWithPath("[].like").description("좋아요"))))
            .when().get("/api/post?page=1&size=5")
            .then().statusCode(HttpStatus.OK.value())
            .extract().body().jsonPath().getList(".", PostResponse.class);

        assertThat(responses).hasSize(5);
    }

    @DisplayName("게시글을 검색한다.")
    @Test
    void searchPost() {
        PostSearchResponse response = given()
            .filter(document("search-posts",
                responseFields(fieldWithPath("results.[].id").description("게시글 Id"),
                    fieldWithPath("results.[].title").description("제목"),
                    fieldWithPath("results.[].category").description("카테고리"),
                    fieldWithPath("results.[].nickname").description("닉네임"),
                    fieldWithPath("results.[].createdAt").description("생성 일자"),
                    fieldWithPath("results.[].updatedAt").description("최근 수정 일자"),
                    fieldWithPath("results.[].notice").description("공지사항"),
                    fieldWithPath("results.[].view").description("조회수"),
                    fieldWithPath("results.[].like").description("좋아요"),
                    fieldWithPath("keyword").description("검색어"),
                    fieldWithPath("type").description("검색 타입"))))
            .when().get("/api/post/search?type={type}&keyword={keyword}", "제목, 내용", "게장")
            .then().statusCode(HttpStatus.OK.value())
            .extract().body().as(PostSearchResponse.class);

        assertThat(response.getResults()).hasSize(2);
    }

    @DisplayName("게시글을 카테고리로 검색한다.")
    @Test
    void searchPostByCategory() {
        List<PostResponse> responses = given()
            .filter(document("category-posts",
                responseFields(fieldWithPath("[].id").description("게시글 Id"),
                    fieldWithPath("[].title").description("제목"),
                    fieldWithPath("[].category").description("카테고리"),
                    fieldWithPath("[].nickname").description("닉네임"),
                    fieldWithPath("[].createdAt").description("생성 일자"),
                    fieldWithPath("[].updatedAt").description("최근 수정 일자"),
                    fieldWithPath("[].notice").description("공지사항"),
                    fieldWithPath("[].view").description("조회수"),
                    fieldWithPath("[].like").description("좋아요"))))
            .when().get("/api/post/category?value={category}", "자유")
            .then().statusCode(HttpStatus.OK.value())
            .extract().body().jsonPath().getList(".", PostResponse.class);

        assertThat(responses).hasSize(3);
    }

    @DisplayName("게시글을 수정한다.")
    @Test
    void updatePost() {
        PostResponse post = postResponses.get(2);

        Map<String, Object> body = new HashMap<>();
        body.put("id", post.getId());
        body.put("title", "니들은 게맛을 몰라~");
        body.put("content", "게장은 다 좋은 것이여~\n게장 만들기 게임도 추가해주세요. ㅎㅎ");
        body.put("category", "건의");
        body.put("password", "345678");

        PostResponse response = given().body(body)
            .filter(document("update-post",
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
            .when().put("/api/post")
            .then().statusCode(HttpStatus.OK.value())
            .extract().body().as(PostResponse.class);

        assertThat(response.getId()).isEqualTo(post.getId());
        assertThat(response.getTitle()).isEqualTo("니들은 게맛을 몰라~");
        assertThat(response.getContent()).isEqualTo("게장은 다 좋은 것이여~\n게장 만들기 게임도 추가해주세요. ㅎㅎ");
        assertThat(response.getCategory()).isEqualTo("건의");
        assertThat(response.getCreatedAt()).isEqualTo(post.getCreatedAt());
    }

    @DisplayName("매우 긴 내용으로 게시글을 수정한다.")
    @Test
    void updatePostHeavyContent() {
        PostResponse post = postResponses.get(2);
        String sentence = "이것은 매우 긴 내용의 글입니다. 긴장해주세요.\n";
        StringBuilder content = new StringBuilder();

        for (int i = 0; i < 10000; i++) {
            content.append(sentence);
        }

        Map<String, Object> body = new HashMap<>();
        body.put("id", post.getId());
        body.put("title", "니들은 게맛을 몰라~");
        body.put("content", content.toString());
        body.put("category", "건의");
        body.put("password", "345678");

        PostResponse response = given().body(body)
            .when().put("/api/post")
            .then().statusCode(HttpStatus.OK.value())
            .extract().body().as(PostResponse.class);

        assertThat(response.getId()).isEqualTo(post.getId());
        assertThat(response.getTitle()).isEqualTo("니들은 게맛을 몰라~");
        assertThat(response.getContent()).isEqualTo(content.toString());
        assertThat(response.getCategory()).isEqualTo("건의");
        assertThat(response.getCreatedAt()).isEqualTo(post.getCreatedAt());
    }

    @DisplayName("게시글에 좋아요를 요청한다.")
    @Test
    void likePost() {
        PostResponse post = postResponses.get(2);

        PostResponse response = given()
            .filter(document("like-post",
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
            .when().patch("/api/post/{id}/like", post.getId())
            .then().statusCode(HttpStatus.OK.value())
            .extract().body().as(PostResponse.class);

        assertThat(response.getId()).isEqualTo(post.getId());
        assertThat(response.getLike()).isEqualTo(1L);
    }

    @DisplayName("게시글을 삭제한다.")
    @Test
    void deletePost() {
        PostResponse post = postResponses.get(3);

        Map<String, Object> body = new HashMap<>();
        body.put("id", post.getId());
        body.put("password", "123456");

        given().body(body)
            .filter(document("delete-post",
                requestFields(fieldWithPath("id").description("검색어"),
                    fieldWithPath("password").description("비밀번호"))))
            .when().delete("/api/post")
            .then().statusCode(HttpStatus.NO_CONTENT.value());

        Response response = 게시글_조회(post.getId());
        response.then().statusCode(HttpStatus.NOT_FOUND.value());
    }

    private Response 게시글_조회(final Long id) {
        return given()
            .when().get("/api/post/{id}", id);
    }
}
