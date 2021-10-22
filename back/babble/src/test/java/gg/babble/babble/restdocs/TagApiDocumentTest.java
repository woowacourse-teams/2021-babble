package gg.babble.babble.restdocs;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import gg.babble.babble.domain.tag.TagName;
import gg.babble.babble.dto.response.AlternativeTagNameResponse;
import gg.babble.babble.dto.response.TagNameResponse;
import gg.babble.babble.dto.response.TagResponse;
import gg.babble.babble.restdocs.client.ResponseRepository;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.RestDocumentationContextProvider;

public class TagApiDocumentTest extends AcceptanceTest {

    private static final String ALTERNATIVE_NAME1 = "silver";
    private static final String ALTERNATIVE_NAME2 = "실딱이";
    private static final String ALTERNATIVE_NAME3 = "2hours";
    private static final String ALTERNATIVE_NAME4 = "3hours";
    private static final String 실버 = "실버";
    private static final String _2시간 = "2시간";
    private static final String _3시간 = "3시간";
    private static final String 솔로랭크 = "솔로랭크";

    private ResponseRepository<TagResponse, Long> tagResponseRepository;

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) throws Exception {
        super.setUp(restDocumentation);

        tagResponseRepository = new ResponseRepository<>(TagResponse::getId);
        localhost_관리자가_추가_됨();
        tagResponseRepository.add(태그가_저장_됨(실버, Arrays.asList(ALTERNATIVE_NAME1, ALTERNATIVE_NAME2)));
        tagResponseRepository.add(태그가_저장_됨(_2시간, Collections.singletonList(ALTERNATIVE_NAME3)));
        tagResponseRepository.add(태그가_저장_됨(_3시간, Collections.singletonList(ALTERNATIVE_NAME4)));
        tagResponseRepository.add(태그가_저장_됨(솔로랭크, Collections.emptyList()));
        localhost_관리자가_제거_됨();
    }

    public static TagResponse 태그가_저장_됨(final String tagName, final List<String> alternativeNames) {
        Map<String, Object> body = new HashMap<>();
        body.put("name", tagName);
        body.put("alternativeNames", alternativeNames);

        return given().body(body)
            .when().post("/api/tags")
            .then().statusCode(HttpStatus.OK.value())
            .extract().body().as(TagResponse.class);

    }

    @DisplayName("전체 태그를 가져오는데 성공하면, 200응답 코드와 전체 태그를 가져온다.")
    @Test
    void tagsGetTest() {
        List<TagResponse> responses = given().filter(document("read-tags",
                responseFields(
                    fieldWithPath("[].id").description("태그 Id"),
                    fieldWithPath("[].name").description("태그 이름"),
                    fieldWithPath("[].alternativeNames[]").description("대체 이름 객체"),
                    fieldWithPath("[].alternativeNames[].id").description("대체 이름 ID"),
                    fieldWithPath("[].alternativeNames[].name").description("대체 이름"))))
            .when().get("/api/tags")
            .then().statusCode(HttpStatus.OK.value())
            .extract().body().jsonPath().getList(".", TagResponse.class);

        assertThat(responses).hasSize(tagResponseRepository.getSize());
        for (TagResponse response : responses) {
            TagResponse expectedTag = tagResponseRepository.get(response.getId());
            assertThat(response.getName()).isEqualTo(expectedTag.getName());
            assertThat(response.getAlternativeNames()).hasSameSizeAs(expectedTag.getAlternativeNames());
            assertThatSameAlternativeTagNames(response.getAlternativeNames(), expectedTag.getAlternativeNames());
        }
    }

    @DisplayName("태그 이름 검색")
    @Test
    void getTagNames() {
        String keyword = "시간";

        List<TagNameResponse> responses = given().filter(document("read-tags-beta",
                responseFields(
                    fieldWithPath("[].id").description("태그 Id"),
                    fieldWithPath("[].name").description("태그 이름")
                )))
            .when().get("/api/beta/tags/names?keyword={keyword}", keyword)
            .then().statusCode(HttpStatus.OK.value())
            .extract().body().jsonPath().getList(".", TagNameResponse.class);

        List<String> foundNames = responses.stream()
            .map(TagNameResponse::getName)
            .collect(Collectors.toList());

        assertThat(foundNames).hasSize(2)
            .contains(_2시간, _3시간);
    }

    public static void assertThatSameAlternativeTagNames(final List<AlternativeTagNameResponse> actual, final List<AlternativeTagNameResponse> expected) {
        assertThat(actual).hasSameSizeAs(expected);

        for (AlternativeTagNameResponse response : actual) {
            AlternativeTagNameResponse expectedAlternativeName = expected.stream()
                .filter(alternativeTagName -> alternativeTagName.getId().equals(response.getId()))
                .findAny()
                .orElseThrow(IllegalAccessError::new);
            assertThat(response.getId()).isEqualTo(expectedAlternativeName.getId());
            assertThat(response.getName()).isEqualTo(expectedAlternativeName.getName());
        }
    }

    @DisplayName("태그를 추가한다.")
    @Test
    void createTag() {
        localhost_관리자가_추가_됨();

        String tagName = "ROOT";
        List<String> alternativeNames = Arrays.asList("blood lord", "darkness of mine", "i'm necessary one");

        Map<String, Object> body = new HashMap<>();
        body.put("name", tagName);
        body.put("alternativeNames", alternativeNames);

        TagResponse response = given().body(body)
            .filter(document("create-tag",
                requestFields(
                    fieldWithPath("name").description("태그 이름"),
                    fieldWithPath("alternativeNames").description("대체 이름")),
                responseFields(
                    fieldWithPath("id").description("태그 ID"),
                    fieldWithPath("name").description("태그 이름"),
                    fieldWithPath("alternativeNames[].id").description("대체 이름 ID"),
                    fieldWithPath("alternativeNames[].name").description("대체 이름"))))
            .when().post("/api/tags")
            .then().statusCode(HttpStatus.OK.value())
            .extract().body().as(TagResponse.class);

        assertThat(response.getId()).isNotNull();
        assertThat(response.getName()).isEqualTo(tagName);

        assertThatEqualsAlternativeNames(response, alternativeNames);
    }

    private void assertThatEqualsAlternativeNames(final TagResponse response, final List<String> alternativeNames) {
        List<String> actualAlternativeNames = response.getAlternativeNames()
            .stream()
            .filter(alternativeTagName -> Objects.nonNull(alternativeTagName.getId()))
            .map(AlternativeTagNameResponse::getName)
            .collect(Collectors.toList());
        assertThat(actualAlternativeNames).hasSameSizeAs(alternativeNames)
            .containsAll(alternativeNames);
    }

    @DisplayName("등록되어 있지 않은 관리자의 경우 태그를 추가할 수 없다.")
    @Test
    void createTagWithInvalidAdministrator() {
        String tagName = "ROOT";
        List<String> alternativeNames = Arrays.asList("blood lord", "darkness of mine", "i'm necessary one");

        Map<String, Object> body = new HashMap<>();
        body.put("name", tagName);
        body.put("alternativeNames", alternativeNames);

        given().body(body)
            .when().post("/api/tags")
            .then().statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("태그를 수정한다.")
    @Test
    void updateTag() {
        localhost_관리자가_추가_됨();

        String tagName = "SILVER ROOT";
        List<String> alternativeNames = Arrays.asList("ultra", "amazing", "necessary root");

        Map<String, Object> body = new HashMap<>();
        body.put("name", tagName);
        body.put("alternativeNames", alternativeNames);
        long idToUpdate = tagResponseRepository.getAnyId();

        TagResponse response = given().body(body)
            .filter(document("update-tag",
                requestFields(
                    fieldWithPath("name").description("태그 이름"),
                    fieldWithPath("alternativeNames").description("대체 이름")),
                responseFields(
                    fieldWithPath("id").description("태그 ID"),
                    fieldWithPath("name").description("태그 이름"),
                    fieldWithPath("alternativeNames[].id").description("대체 이름 ID"),
                    fieldWithPath("alternativeNames[].name").description("대체 이름"))))
            .when().put("/api/tags/{id}", idToUpdate)
            .then().statusCode(HttpStatus.OK.value())
            .extract().body().as(TagResponse.class);

        assertThat(response.getId()).isEqualTo(idToUpdate);
        assertThat(response.getName()).isEqualTo(tagName);
        assertThatEqualsAlternativeNames(response, alternativeNames);
    }

    @DisplayName("등록되어 있지 않은 관리자의 경우 태그를 수정할 수 없다.")
    @Test
    void updateTagWithInvalidAdministrator() {
        String tagName = "SILVER ROOT";
        List<String> alternativeNames = Arrays.asList("ultra", "amazing", "necessary root");
        long idToUpdate = tagResponseRepository.getAnyId();

        Map<String, Object> body = new HashMap<>();
        body.put("name", tagName);
        body.put("alternativeNames", alternativeNames);

        given().body(body)
            .when().put("/api/tags/{id}", idToUpdate)
            .then().statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("태그를 삭제한다")
    @Test
    void deleteTag() {
        localhost_관리자가_추가_됨();

        long idToDelete = tagResponseRepository.getAnyId();
        given().filter(document("delete-tag"))
            .when().delete("/api/tags/{id}", idToDelete)
            .then().statusCode(HttpStatus.NO_CONTENT.value());

        태그가_존재하지_않음(idToDelete);
    }

    private void 태그가_존재하지_않음(final Long id) {
        List<TagResponse> responses = given()
            .when().get("/api/tags")
            .then().statusCode(HttpStatus.OK.value())
            .extract().body().jsonPath().getList(".", TagResponse.class);

        for (TagResponse response : responses) {
            assertThat(response.getId()).isNotEqualTo(id);
        }
    }

    @DisplayName("등록되어 있지 않은 관리자의 경우 게임을 삭제할 수 없다.")
    @Test
    void deleteTagWithInvalidAdministrator() {
        long idToDelete = tagResponseRepository.getAnyId();

        given()
            .when().delete("/api/tags/{id}", idToDelete)
            .then().statusCode(HttpStatus.UNAUTHORIZED.value());
    }
}
