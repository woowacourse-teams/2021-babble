package gg.babble.babble.restdocs;

import static org.assertj.core.api.Assertions.assertThat;
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
import gg.babble.babble.domain.game.AlternativeName;
import gg.babble.babble.domain.game.Game;
import gg.babble.babble.domain.game.Games;
import gg.babble.babble.dto.response.GameImageResponse;
import gg.babble.babble.dto.response.GameWithImageResponse;
import gg.babble.babble.dto.response.IndexPageGameResponse;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.context.WebApplicationContext;

public class GameApiDocumentTest extends ApiDocumentTest {

    private final List<Game> games = new ArrayList<>();

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        super.setUp(webApplicationContext, restDocumentation);

        games.add(gameRepository.save(new Game("League Of Legends1", "image1")));
        games.add(gameRepository.save(new Game("League Of Legends2", "image2")));
        games.add(gameRepository.save(new Game("League Of Legends3", "image3")));

        alternativeNameRepository.save(new AlternativeName("롤1", games.get(0)));
        alternativeNameRepository.save(new AlternativeName("리오레1", games.get(0)));
        alternativeNameRepository.save(new AlternativeName("롤2", games.get(1)));
    }

    @DisplayName("게임 리스트 조회")
    @Test
    void findAllGames() throws Exception {

        final List<IndexPageGameResponse> expected = IndexPageGameResponse.listFrom(new Games(games));

        final MvcResult mvcResult = mockMvc.perform(get("/api/games")
            .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andDo(document("read-games",
                responseFields(
                    fieldWithPath("[].id").description("게임 Id"),
                    fieldWithPath("[].name").description("게임 이름"),
                    fieldWithPath("[].headCount").description("게임의 참가자 수"),
                    fieldWithPath("[].thumbnail").description("썸네일"),
                    fieldWithPath("[].alternativeNames").description("대체 이름"))))
            .andReturn();

        final List<IndexPageGameResponse> responses = Arrays.asList(getResponseAs(mvcResult, IndexPageGameResponse[].class));

        assertThat(responses).usingRecursiveComparison().isEqualTo(expected);
    }

    @DisplayName("단일 게임 이미지 조회")
    @Test
    void findGameImageById() throws Exception {
        final GameImageResponse expected = GameImageResponse.from(games.get(0));

        final MvcResult mvcResult = mockMvc.perform(get("/api/games/" + games.get(0).getId() + "/images")
            .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andDo(document("read-game-image",
                responseFields(
                    fieldWithPath("gameId").description("게임 Id"),
                    fieldWithPath("image").description("이미지 URL"))))
            .andReturn();

        final GameImageResponse response = getResponseAs(mvcResult, GameImageResponse.class);

        assertThat(response).usingRecursiveComparison().isEqualTo(expected);
    }

    @DisplayName("전체 게임 이미지 목록 조회")
    @Test
    void findGameImages() throws Exception {
        final List<GameImageResponse> expected = games.stream()
            .map(GameImageResponse::from)
            .collect(Collectors.toList());

        final MvcResult mvcResult = mockMvc.perform(get("/api/games/images")
            .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andDo(document("read-game-images",
                responseFields(fieldWithPath("[].gameId").description("게임 Id"),
                    fieldWithPath("[].image").description("이미지 URL"))
                )
            )
            .andReturn();

        final List<GameImageResponse> responses = Arrays.asList(getResponseAs(mvcResult, GameImageResponse[].class));

        assertThat(responses).usingRecursiveComparison().isEqualTo(expected);
    }

    @DisplayName("단일 게임 조회")
    @Test
    void findGameById() throws Exception {

        final GameWithImageResponse expected = GameWithImageResponse.from(games.get(0));

        final MvcResult mvcResult = mockMvc.perform(
            get("/api/games/" + games.get(0).getId())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("utf-8"))
            .andExpect(status().isOk())
            .andDo(document("read-game",
                responseFields(fieldWithPath("id").description("게임 Id"),
                    fieldWithPath("name").description("게임 이름"),
                    fieldWithPath("thumbnail").description("썸네일"),
                    fieldWithPath("alternativeNames").description("대체 이름"))))
            .andReturn();

        final GameWithImageResponse response = getResponseAs(mvcResult, GameWithImageResponse.class);

        assertThat(response).usingRecursiveComparison().isEqualTo(expected);
    }

    @DisplayName("게임을 추가한다.")
    @Test
    void createGame() throws Exception {
        administratorRepository.save(new Administrator("127.0.0.1", "localhost"));
        String gameName = "League Of Legends";
        String thumbnail = "image.png";
        Set<String> alternativeNames = new HashSet<>(Arrays.asList("롤", "리오레"));

        Map<String, Object> body = new HashMap<>();
        body.put("name", gameName);
        body.put("thumbnail", thumbnail);
        body.put("alternativeNames", alternativeNames);

        GameWithImageResponse expected = new GameWithImageResponse(null, gameName, thumbnail, alternativeNames);

        final MvcResult mvcResult = mockMvc.perform(post("/api/games")
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(body)).characterEncoding("utf-8"))
            .andDo(document("insert-game",
                requestFields(
                    fieldWithPath("name").description("게임 이름"),
                    fieldWithPath("thumbnail").description("게임 썸네일 URL"),
                    fieldWithPath("alternativeNames").description("대체 이름")
                ),
                responseFields(
                    fieldWithPath("id").description("게임 ID"),
                    fieldWithPath("name").description("게임 이름"),
                    fieldWithPath("thumbnail").description("게임 썸네일 URL"),
                    fieldWithPath("alternativeNames").description("대체 이름")
                )
            ))
            .andReturn();

        final GameWithImageResponse response = getResponseAs(mvcResult, GameWithImageResponse.class);

        assertThat(response).usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(expected);
    }

    @DisplayName("등록되어 있지 않은 관리자의 경우 게임을 추가할 수 없다.")
    @Test
    void createGameWithInvalidAdministrator() throws Exception {
        String gameName = "League Of Legends";
        String thumbnail = "image.png";
        Set<String> alternativeNames = new HashSet<>(Arrays.asList("롤", "리오레"));

        Map<String, Object> body = new HashMap<>();
        body.put("name", gameName);
        body.put("thumbnail", thumbnail);
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
        String gameName = "League Of Legeno";
        String thumbnail = "image.png";
        Set<String> alternativeNames = new HashSet<>(Arrays.asList("롤", "리오레"));

        Map<String, Object> body = new HashMap<>();
        body.put("name", gameName);
        body.put("thumbnail", thumbnail);
        body.put("alternativeNames", alternativeNames);

        GameWithImageResponse expected = new GameWithImageResponse(null, gameName, thumbnail, alternativeNames);

        final MvcResult mvcResult = mockMvc.perform(put("/api/games/" + games.get(0).getId())
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(body)).characterEncoding("utf-8"))
            .andDo(document("update-game",
                requestFields(
                    fieldWithPath("name").description("게임 이름"),
                    fieldWithPath("thumbnail").description("게임 썸네일 URL"),
                    fieldWithPath("alternativeNames").description("대체 이름")
                ),
                responseFields(
                    fieldWithPath("id").description("게임 ID"),
                    fieldWithPath("name").description("게임 이름"),
                    fieldWithPath("thumbnail").description("게임 썸네일 URL"),
                    fieldWithPath("alternativeNames").description("대체 이름")
                )
            )).andReturn();

        final GameWithImageResponse response = getResponseAs(mvcResult, GameWithImageResponse.class);
        assertThat(response).usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(expected);
    }

    @DisplayName("등록되어 있지 않은 관리자의 경우 게임을 수정할 수 없다.")
    @Test
    void editGameWithInvalidIp() throws Exception {
        String gameName = "League Of Legeno";
        String thumbnail = "image.png";
        Set<String> alternativeNames = new HashSet<>(Arrays.asList("롤", "리오레"));

        Map<String, Object> body = new HashMap<>();
        body.put("name", gameName);
        body.put("thumbnail", thumbnail);
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
