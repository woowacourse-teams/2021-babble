package gg.babble.babble.restdocs;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import gg.babble.babble.dto.response.AlternativeGameNameResponse;
import gg.babble.babble.dto.response.GameImageResponse;
import gg.babble.babble.dto.response.GameNameResponse;
import gg.babble.babble.dto.response.GameWithImageResponse;
import gg.babble.babble.dto.response.IndexPageGameResponse;
import gg.babble.babble.restdocs.client.ResponseRepository;
import gg.babble.babble.restdocs.preprocessor.BigListPreprocessor;
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

public class GameApiDocumentTest extends AcceptanceTest {

    private static final String ALTERNATIVE_NAME1 = "롤";
    private static final String ALTERNATIVE_NAME2 = "리오레";
    private static final String ALTERNATIVE_NAME3 = "오버워치";

    private static final String IMAGE_1 = "img1";
    private static final String IMAGE_2 = "img2";
    private static final String IMAGE_3 = "img3";
    private static final int PAGE_SIZE = 16;
    private final List<String> images = Arrays.asList(IMAGE_1, IMAGE_2, IMAGE_3);
    private ResponseRepository<GameWithImageResponse, Long> gameWithImageResponseRepository;

    public static GameWithImageResponse 게임이_저장_됨(final String gameName, final List<String> images, final List<String> alternativeNames) {
        Map<String, Object> body = new HashMap<>();
        body.put("name", gameName);
        body.put("images", images);
        body.put("alternativeNames", alternativeNames);

        return given()
            .body(body)
            .when().post("/api/games")
            .then().statusCode(HttpStatus.CREATED.value())
            .extract().body().as(GameWithImageResponse.class);
    }

    @BeforeEach
    protected void setUp(final RestDocumentationContextProvider restDocumentation) throws Exception {
        super.setUp(restDocumentation);
        gameWithImageResponseRepository = new ResponseRepository<>(GameWithImageResponse::getId);

        localhost_관리자가_추가_됨();

        for (int i = 0; i < 15; i++) {
            gameWithImageResponseRepository.add(
                게임이_저장_됨("League Of Legends" + i, Collections.singletonList(IMAGE_1), Arrays.asList(ALTERNATIVE_NAME1 + i, ALTERNATIVE_NAME2 + i))
            );
            gameWithImageResponseRepository.add(
                게임이_저장_됨("Overwatch" + i, Collections.singletonList(IMAGE_2), Collections.singletonList(ALTERNATIVE_NAME3 + i))
            );
        }
        localhost_관리자가_제거_됨();
    }

    @DisplayName("게임 리스트 조회")
    @Test
    void findAllGames() {
        List<IndexPageGameResponse> responses = given()
            .filter(document("read-games",
                preprocessResponse(new BigListPreprocessor()),
                responseFields(
                    fieldWithPath("[].id").description("게임 Id"),
                    fieldWithPath("[].name").description("게임 이름"),
                    fieldWithPath("[].headCount").description("게임의 참가자 수"),
                    fieldWithPath("[].images").description("이미지 목록"),
                    fieldWithPath("[].alternativeNames[]").description("대체 이름 객체"),
                    fieldWithPath("[].alternativeNames[].id").description("대체 이름 ID"),
                    fieldWithPath("[].alternativeNames[].name").description("대체 이름"))))
            .when().get("/api/games")
            .then().statusCode(HttpStatus.OK.value())
            .extract().body().jsonPath().getList(".", IndexPageGameResponse.class);

        assertThat(responses).hasSize(gameWithImageResponseRepository.getSize());
        for (IndexPageGameResponse response : responses) {
            GameWithImageResponse expectedGame = gameWithImageResponseRepository.get(response.getId());
            assertThat(response.getName()).isEqualTo(expectedGame.getName());
            assertThat(response.getHeadCount()).isEqualTo(0);
            assertThat(response.getImages()).hasSameSizeAs(expectedGame.getImages())
                .containsAll(expectedGame.getImages());
            assertThatSameAlternativeNames(response.getAlternativeNames(), expectedGame.getAlternativeNames());
        }
    }

    @DisplayName("게임 리스트 조회(페이지네이션)")
    @Test
    void findGameImagesWithPagination() {
        List<IndexPageGameResponse> responses = given()
            .when().get("/api/beta/games?page=1")
            .then().statusCode(HttpStatus.OK.value())
            .extract().body().jsonPath().getList(".", IndexPageGameResponse.class);

        assertThat(responses).hasSize(PAGE_SIZE);
    }

    @DisplayName("게임 이름으로 게임 리스트 조회")
    @Test
    void findGameImageWithNameAndPagination() {
        String nameToSearch = "롤";
        List<IndexPageGameResponse> responses = given()
            .filter(document("read-games-beta",
                preprocessResponse(new BigListPreprocessor()),
                responseFields(
                    fieldWithPath("[].id").description("게임 Id"),
                    fieldWithPath("[].name").description("게임 이름"),
                    fieldWithPath("[].headCount").description("게임의 참가자 수"),
                    fieldWithPath("[].images").description("이미지 목록"),
                    fieldWithPath("[].alternativeNames[]").description("대체 이름 객체"),
                    fieldWithPath("[].alternativeNames[].id").description("대체 이름 ID"),
                    fieldWithPath("[].alternativeNames[].name").description("대체 이름"))))
            .when().get("/api/beta/games?keyword=" + nameToSearch + "&page=1")
            .then().statusCode(HttpStatus.OK.value())
            .extract().body().jsonPath().getList(".", IndexPageGameResponse.class);

        assertThat(responses).hasSize(15);
        for (IndexPageGameResponse response : responses) {
            boolean containsInAlternativeNames = response.getAlternativeNames()
                .stream()
                .map(AlternativeGameNameResponse::getName)
                .anyMatch(alternativeName -> alternativeName.contains(nameToSearch));

            boolean containsInName = response.getName().contains(nameToSearch);

            assertThat(containsInName || containsInAlternativeNames).isTrue();
        }
    }

    @DisplayName("단일 게임 이미지 조회")
    @Test
    void findGameImageById() {
        long idToFind = gameWithImageResponseRepository.getAnyId();

        GameImageResponse response = given()
            .filter(document("read-game-images",
                responseFields(
                    fieldWithPath("gameId").description("게임 Id"),
                    fieldWithPath("images").description("게임 이미지 목록"))))
            .when().get("/api/games/{id}/images", idToFind)
            .then().statusCode(HttpStatus.OK.value())
            .extract().body().as(GameImageResponse.class);

        assertThat(response.getGameId()).isEqualTo(idToFind);
        assertThat(response.getImages()).hasSize(1);
        assertThat(response.getImages().get(0)).isEqualTo(IMAGE_1);
    }

    @DisplayName("게임 이름 검색")
    @Test
    void findGameNames() {
        String findToKeyword = "롤";

        List<GameNameResponse> responses = given().filter(document("read-games-names",
                preprocessResponse(new BigListPreprocessor()),
                responseFields(
                    fieldWithPath("[].id").description("게임 Id"),
                    fieldWithPath("[].name").description("게임 이름")
                )))
            .when().get("/api/beta/games/names?keyword={keyword}", findToKeyword)
            .then().statusCode(HttpStatus.OK.value())
            .extract().body().jsonPath().getList(".", GameNameResponse.class);

        assertThat(responses).hasSize(15);
    }

    @DisplayName("전체 게임 이미지 목록 조회")
    @Test
    void findGameImages() {
        List<GameImageResponse> responses = given()
            .filter(document("read-games-images",
                responseFields(fieldWithPath("[].gameId").description("게임 Id"),
                    fieldWithPath("[].images").description("이미지 URL"))))
            .when().get("/api/games/images")
            .then().statusCode(HttpStatus.OK.value())
            .extract().body().jsonPath().getList(".", GameImageResponse.class);

        assertThat(responses).hasSize(gameWithImageResponseRepository.getSize());
        for (GameImageResponse response : responses) {
            GameWithImageResponse expectedGame = gameWithImageResponseRepository.get(response.getGameId());
            assertThat(response.getImages()).hasSameSizeAs(expectedGame.getImages())
                .containsAll(expectedGame.getImages());
        }
    }

    @DisplayName("단일 게임 조회")
    @Test
    void findGameById() {
        long idToFind = gameWithImageResponseRepository.getAnyId();

        GameWithImageResponse response = given()
            .filter(document("read-game",
                responseFields(fieldWithPath("id").description("게임 Id"),
                    fieldWithPath("name").description("게임 이름"),
                    fieldWithPath("images").description("게임 이미지 목록"),
                    fieldWithPath("alternativeNames[]").description("대체 이름 객체"),
                    fieldWithPath("alternativeNames[].id").description("대체 이름 ID"),
                    fieldWithPath("alternativeNames[].name").description("대체 이름"))))
            .when().get("/api/games/{id}", idToFind)
            .then().statusCode(HttpStatus.OK.value())
            .extract().body().as(GameWithImageResponse.class);

        GameWithImageResponse expected = gameWithImageResponseRepository.get(idToFind);
        assertThat(response.getId()).isEqualTo(expected.getId());
        assertThat(response.getName()).isEqualTo(expected.getName());
        assertThat(response.getImages()).hasSameSizeAs(expected.getImages())
            .containsAll(expected.getImages());
        assertThatSameAlternativeNames(response.getAlternativeNames(), expected.getAlternativeNames());
    }

    private void assertThatSameAlternativeNames(final List<AlternativeGameNameResponse> actual, final List<AlternativeGameNameResponse> expected) {
        assertThat(actual).hasSameSizeAs(expected);

        for (AlternativeGameNameResponse response : actual) {
            AlternativeGameNameResponse expectedAlternativeName = expected.stream()
                .filter(alternativeGameName -> alternativeGameName.getId().equals(response.getId()))
                .findAny()
                .orElseThrow(IllegalAccessError::new);
            assertThat(response.getName()).isEqualTo(expectedAlternativeName.getName());
        }
    }

    @DisplayName("게임을 추가한다.")
    @Test
    void createGame() {
        localhost_관리자가_추가_됨();
        String gameName = "League Of Legends";
        List<String> images = Arrays.asList("img1", "img2", "img3");
        List<String> alternativeNames = Collections.singletonList("롤");

        Map<String, Object> body = new HashMap<>();
        body.put("name", gameName);
        body.put("images", images);
        body.put("alternativeNames", alternativeNames);

        GameWithImageResponse response = given()
            .body(body)
            .filter(document("create-game",
                requestFields(
                    fieldWithPath("name").description("게임 이름"),
                    fieldWithPath("images").description("게임 이미지 목록"),
                    fieldWithPath("alternativeNames").description("대체 이름")
                ),
                responseFields(
                    fieldWithPath("id").description("게임 ID"),
                    fieldWithPath("name").description("게임 이름"),
                    fieldWithPath("images").description("게임 이미지 목록"),
                    fieldWithPath("alternativeNames[]").description("대체 이름 객체"),
                    fieldWithPath("alternativeNames[].id").description("대체 이름 ID"),
                    fieldWithPath("alternativeNames[].name").description("대체 이름"))))
            .when().post("/api/games")
            .then().statusCode(HttpStatus.CREATED.value())
            .extract().body().as(GameWithImageResponse.class);

        assertThat(response.getId()).isNotNull();
        assertThat(response.getName()).isEqualTo(gameName);
        assertThat(response.getImages()).hasSameSizeAs(images)
            .containsAll(images);
        List<String> actualAlternativeNames = response.getAlternativeNames()
            .stream()
            .filter(alternativeGameName -> Objects.nonNull(alternativeGameName.getId()))
            .map(AlternativeGameNameResponse::getName)
            .collect(Collectors.toList());

        assertThat(actualAlternativeNames).hasSameSizeAs(alternativeNames)
            .hasSameSizeAs(alternativeNames);
    }

    @DisplayName("등록되어 있지 않은 관리자의 경우 게임을 추가할 수 없다.")
    @Test
    void createGameWithInvalidAdministrator() {
        String gameName = "League Of Legends";
        List<String> alternativeNames = Collections.singletonList("롤");

        Map<String, Object> body = new HashMap<>();
        body.put("name", gameName);
        body.put("images", images);
        body.put("alternativeNames", alternativeNames);

        given().body(body)
            .when().post("/api/games")
            .then().statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("게임을 수정한다.")
    @Test
    void editGame() {
        localhost_관리자가_추가_됨();
        String gameName = "League Of Legends";
        List<String> alternativeNames = Collections.singletonList("롤");
        long idToUpdate = gameWithImageResponseRepository.getAnyId();

        Map<String, Object> body = new HashMap<>();
        body.put("name", gameName);
        body.put("images", images);
        body.put("alternativeNames", alternativeNames);

        GameWithImageResponse response = given().body(body)
            .filter(document("update-game",
                requestFields(
                    fieldWithPath("name").description("게임 이름"),
                    fieldWithPath("images").description("게임 이미지 목록"),
                    fieldWithPath("alternativeNames").description("대체 이름")
                ), responseFields(
                    fieldWithPath("id").description("게임 ID"),
                    fieldWithPath("name").description("게임 이름"),
                    fieldWithPath("images").description("게임 이미지 목록"),
                    fieldWithPath("alternativeNames[].id").description("대체 이름 Id"),
                    fieldWithPath("alternativeNames[].name").description("대체 이름"))))
            .when().put("/api/games/{id}", idToUpdate)
            .then().statusCode(HttpStatus.OK.value())
            .extract().body().as(GameWithImageResponse.class);

        assertThat(response.getId()).isEqualTo(idToUpdate);
        assertThat(response.getName()).isEqualTo(gameName);
        assertThat(response.getImages()).hasSameSizeAs(images)
            .containsAll(images);
        List<String> actualAlternativeNames = response.getAlternativeNames()
            .stream()
            .map(AlternativeGameNameResponse::getName)
            .collect(Collectors.toList());
        assertThat(actualAlternativeNames).hasSameSizeAs(alternativeNames)
            .hasSameSizeAs(alternativeNames);
    }

    @DisplayName("등록되어 있지 않은 관리자의 경우 게임을 수정할 수 없다.")
    @Test
    void editGameWithInvalidIp() {
        String gameName = "League Of Legends";
        List<String> alternativeNames = Collections.singletonList("롤");
        long idToUpdate = gameWithImageResponseRepository.getAnyId();

        Map<String, Object> body = new HashMap<>();
        body.put("name", gameName);
        body.put("images", images);
        body.put("alternativeNames", alternativeNames);

        given().body(body)
            .when().put("/api/games/{id}", idToUpdate)
            .then().statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("게임을 삭제한다")
    @Test
    void removeGame() {
        long idToDelete = gameWithImageResponseRepository.getAnyId();
        localhost_관리자가_추가_됨();

        given().filter(document("delete-game"))
            .when().delete("/api/games/{id}", idToDelete)
            .then().statusCode(HttpStatus.NO_CONTENT.value());

        게임을_찾지_못_한다(idToDelete);
    }

    private void 게임을_찾지_못_한다(final Long id) {
        given()
            .when().get("/api/games/{id}", id)
            .then().statusCode(HttpStatus.NOT_FOUND.value());
    }

    @DisplayName("등록되어 있지 않은 관리자의 경우 게임을 삭제할 수 없다.")
    @Test
    void removeGameWithInvalidIp() {
        long idToDelete = gameWithImageResponseRepository.getAnyId();

        given()
            .when().delete("/api/games/{id}", idToDelete)
            .then().statusCode(HttpStatus.UNAUTHORIZED.value());
    }
}
