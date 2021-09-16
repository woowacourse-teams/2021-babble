package gg.babble.babble.restdocs;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import gg.babble.babble.domain.admin.Administrator;
import gg.babble.babble.domain.game.AlternativeGameName;
import gg.babble.babble.domain.game.Game;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.context.WebApplicationContext;

public class GameApiDocumentTest extends ApiDocumentTest {

    private static final String ALTERNATIVE_NAME1 = "롤1";
    private static final String ALTERNATIVE_NAME2 = "리오레1";
    private static final String ALTERNATIVE_NAME3 = "롤2";

    private static final String IMAGE_1 = "img1";
    private static final String IMAGE_2 = "img2";
    private static final String IMAGE_3 = "img3";
    private final List<Game> games = new ArrayList<>();
    private final List<String> images = Arrays.asList(IMAGE_1, IMAGE_2, IMAGE_3);

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) throws Exception {
        super.setUp(webApplicationContext, restDocumentation);

        games.add(gameRepository.save(new Game("League Of Legends1", Collections.singletonList(IMAGE_1))));
        games.add(gameRepository.save(new Game("League Of Legends2", Arrays.asList(IMAGE_1, IMAGE_2))));
        games.add(gameRepository.save(new Game("League Of Legends3", images)));

        alternativeGameNameRepository.save(new AlternativeGameName(ALTERNATIVE_NAME1, games.get(0)));
        alternativeGameNameRepository.save(new AlternativeGameName(ALTERNATIVE_NAME2, games.get(0)));
        alternativeGameNameRepository.save(new AlternativeGameName(ALTERNATIVE_NAME3, games.get(1)));
    }

    @DisplayName("게임 리스트 조회")
    @Test
    void findAllGames() throws Exception {
        mockMvc.perform(get("/api/games")
                .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(games.get(0).getId()))
            .andExpect(jsonPath("$[0].name").value(games.get(0).getName()))
            .andExpect(jsonPath("$[0].headCount").value(0))
            .andExpect(jsonPath("$[0].images").value(hasSize(1)))
            .andExpect(jsonPath("$[0].images").value(containsInAnyOrder(IMAGE_1)))
            .andExpect(jsonPath("$[0].alternativeNames").value(hasSize(2)))
            .andExpect(jsonPath("$[0].alternativeNames").value(containsInAnyOrder(ALTERNATIVE_NAME1, ALTERNATIVE_NAME2)))
            .andExpect(jsonPath("$[1].id").value(games.get(1).getId()))
            .andExpect(jsonPath("$[1].name").value(games.get(1).getName()))
            .andExpect(jsonPath("$[1].headCount").value(0))
            .andExpect(jsonPath("$[1].images").value(hasSize(2)))
            .andExpect(jsonPath("$[1].images").value(containsInAnyOrder(IMAGE_1, IMAGE_2)))
            .andExpect(jsonPath("$[1].alternativeNames").value(hasSize(1)))
            .andExpect(jsonPath("$[1].alternativeNames[0]").value(ALTERNATIVE_NAME3))
            .andExpect(jsonPath("$[2].id").value(games.get(2).getId()))
            .andExpect(jsonPath("$[2].name").value(games.get(2).getName()))
            .andExpect(jsonPath("$[2].headCount").value(0))
            .andExpect(jsonPath("$[2].images").value(hasSize(3)))
            .andExpect(jsonPath("$[2].images").value(containsInAnyOrder(IMAGE_1, IMAGE_2, IMAGE_3)))
            .andExpect(jsonPath("$[2].alternativeNames").value(hasSize(0)))

            .andDo(document("read-games",
                responseFields(
                    fieldWithPath("[].id").description("게임 Id"),
                    fieldWithPath("[].name").description("게임 이름"),
                    fieldWithPath("[].headCount").description("게임의 참가자 수"),
                    fieldWithPath("[].images").description("이미지 목록"),
                    fieldWithPath("[].alternativeNames").description("대체 이름"))));
    }

    @DisplayName("단일 게임 이미지 조회")
    @Test
    void findGameImageById() throws Exception {
        mockMvc.perform(get("/api/games/" + games.get(0).getId() + "/images")
                .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.gameId").value(games.get(0).getId()))
            .andExpect(jsonPath("$.images").value(IMAGE_1))

            .andDo(document("read-game-image",
                responseFields(
                    fieldWithPath("gameId").description("게임 Id"),
                    fieldWithPath("images").description("게임 이미지 목록"))));
    }

    @DisplayName("전체 게임 이미지 목록 조회")
    @Test
    void findGameImages() throws Exception {
        mockMvc.perform(get("/api/games/images")
                .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].gameId").value(games.get(0).getId()))
            .andExpect(jsonPath("$[0].images").value(IMAGE_1))
            .andExpect(jsonPath("$[1].gameId").value(games.get(1).getId()))
            .andExpect(jsonPath("$[1].images").value(containsInAnyOrder(IMAGE_1, IMAGE_2)))
            .andExpect(jsonPath("$[2].gameId").value(games.get(2).getId()))
            .andExpect(jsonPath("$[2].images").value(containsInAnyOrder(IMAGE_1, IMAGE_2, IMAGE_3)))

            .andDo(document("read-game-images",
                    responseFields(fieldWithPath("[].gameId").description("게임 Id"),
                        fieldWithPath("[].images").description("이미지 URL"))
                )
            );
    }

    @DisplayName("단일 게임 조회")
    @Test
    void findGameById() throws Exception {

        mockMvc.perform(get("/api/games/" + games.get(0).getId())
                .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(games.get(0).getId()))
            .andExpect(jsonPath("$.name").value(games.get(0).getName()))
            .andExpect(jsonPath("$.images").value(IMAGE_1))
            .andExpect(jsonPath("$.alternativeNames").value(hasSize(2)))
            .andExpect(jsonPath("$.alternativeNames").value(containsInAnyOrder(ALTERNATIVE_NAME1, ALTERNATIVE_NAME2)))

            .andDo(document("read-game",
                responseFields(fieldWithPath("id").description("게임 Id"),
                    fieldWithPath("name").description("게임 이름"),
                    fieldWithPath("images").description("게임 이미지 목록"),
                    fieldWithPath("alternativeNames").description("대체 이름"))));
    }

    @DisplayName("게임을 추가한다.")
    @Test
    void createGame() throws Exception {
        administratorRepository.save(new Administrator("127.0.0.1", "localhost"));
        String gameName = "League Of Legends";
        List<String> images = Arrays.asList("img1", "img2", "img3");
        List<String> alternativeNames = Collections.singletonList("롤");

        Map<String, Object> body = new HashMap<>();
        body.put("name", gameName);
        body.put("images", images);
        body.put("alternativeNames", alternativeNames);

        mockMvc.perform(post("/api/games")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(body)).characterEncoding("utf-8"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").isNumber())
            .andExpect(jsonPath("$.name").value(gameName))
            .andExpect(jsonPath("$.images").value(hasSize(3)))
            .andExpect(jsonPath("$.images[0]").value(IMAGE_1))
            .andExpect(jsonPath("$.alternativeNames").value(hasSize(1)))
            .andExpect(jsonPath("$.alternativeNames").value(alternativeNames.get(0)))

            .andDo(document("insert-game",
                requestFields(
                    fieldWithPath("name").description("게임 이름"),
                    fieldWithPath("images").description("게임 이미지 목록"),
                    fieldWithPath("alternativeNames").description("대체 이름")
                ),
                responseFields(
                    fieldWithPath("id").description("게임 ID"),
                    fieldWithPath("name").description("게임 이름"),
                    fieldWithPath("images").description("게임 이미지 목록"),
                    fieldWithPath("alternativeNames").description("대체 이름")
                )
            ));
    }

    @DisplayName("등록되어 있지 않은 관리자의 경우 게임을 추가할 수 없다.")
    @Test
    void createGameWithInvalidAdministrator() throws Exception {
        String gameName = "League Of Legends";
        List<String> alternativeNames = Collections.singletonList("롤");

        Map<String, Object> body = new HashMap<>();
        body.put("name", gameName);
        body.put("images", images);
        body.put("alternativeNames", alternativeNames);

        mockMvc.perform(post("/api/games")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(body)).characterEncoding("utf-8"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isUnauthorized());
    }

    @DisplayName("게임을 수정한다.")
    @Test
    void editGame() throws Exception {
        administratorRepository.save(new Administrator("127.0.0.1", "localhost"));
        String gameName = "League Of Legends";
        List<String> alternativeNames = Collections.singletonList("롤");

        Map<String, Object> body = new HashMap<>();
        body.put("name", gameName);
        body.put("images", images);
        body.put("alternativeNames", alternativeNames);

        mockMvc.perform(put("/api/games/" + games.get(0).getId())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(body)).characterEncoding("utf-8"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").isNumber())
            .andExpect(jsonPath("$.name").value(gameName))
            .andExpect(jsonPath("$.images").value(hasSize(3)))
            .andExpect(jsonPath("$.images").value(containsInAnyOrder(IMAGE_1, IMAGE_2, IMAGE_3)))
            .andDo(document("insert-game",
                requestFields(
                    fieldWithPath("name").description("게임 이름"),
                    fieldWithPath("images").description("게임 이미지 목록"),
                    fieldWithPath("alternativeNames").description("대체 이름")
                ),
                responseFields(
                    fieldWithPath("id").description("게임 ID"),
                    fieldWithPath("name").description("게임 이름"),
                    fieldWithPath("images").description("게임 이미지 목록"),
                    fieldWithPath("alternativeNames").description("대체 이름")
                )
            ));
    }

    @DisplayName("등록되어 있지 않은 관리자의 경우 게임을 수정할 수 없다.")
    @Test
    void editGameWithInvalidIp() throws Exception {
        String gameName = "League Of Legends";
        List<String> alternativeNames = Collections.singletonList("롤");

        Map<String, Object> body = new HashMap<>();
        body.put("name", gameName);
        body.put("images", images);
        body.put("alternativeNames", alternativeNames);

        mockMvc.perform(put("/api/games/" + games.get(0).getId())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(body)).characterEncoding("utf-8"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isUnauthorized());
    }

    @DisplayName("게임을 삭제한다")
    @Test
    void removeGame() throws Exception {
        administratorRepository.save(new Administrator("127.0.0.1", "localhost"));
        mockMvc.perform(delete("/api/games/" + games.get(0).getId()).
                accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isNoContent())
            .andDo(document("delete-game"));

        mockMvc.perform(get("/api/games/" + games.get(0).getId())
                .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isNotFound());
    }

    @DisplayName("등록되어 있지 않은 관리자의 경우 게임을 삭제할 수 없다.")
    @Test
    void removeGameWithInvalidIp() throws Exception {
        mockMvc.perform(delete("/api/games/" + games.get(0).getId()).
                accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isUnauthorized());
    }
}
