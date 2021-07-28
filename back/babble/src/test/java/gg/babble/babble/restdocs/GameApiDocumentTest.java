package gg.babble.babble.restdocs;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import gg.babble.babble.ApplicationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
public class GameApiDocumentTest extends ApplicationTest {

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .apply(documentationConfiguration(restDocumentation))
            .alwaysDo(document("{method-name}", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
            .build();
    }

    @DisplayName("게임 리스트 조회")
    @Test
    void findAllGames() throws Exception {
        mockMvc.perform(get("/api/games")
            .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1L))
            .andExpect(jsonPath("$[0].name").value("League Of Legends"))
            .andExpect(jsonPath("$[0].headCount").value(1))
            .andExpect(jsonPath("$[0].thumbnail").value("https://static-cdn.jtvnw.net/ttv-boxart/League%20of%20Legends-1080x1436.jpg"))
            .andExpect(jsonPath("$[1].id").value(2L))
            .andExpect(jsonPath("$[1].name").value("Overwatch"))
            .andExpect(jsonPath("$[1].headCount").value(0))
            .andExpect(jsonPath("$[1].thumbnail").value("https://static-cdn.jtvnw.net/ttv-static/404_boxart-1080x1436.jpg"))
            .andExpect(jsonPath("$[2].id").value(3L))
            .andExpect(jsonPath("$[2].name").value("Apex Legend"))
            .andExpect(jsonPath("$[2].headCount").value(0))
            .andExpect(jsonPath("$[2].thumbnail").value("https://static-cdn.jtvnw.net/ttv-static/404_boxart-1080x1436.jpg"))

            .andDo(document("read-games",
                responseFields(
                    fieldWithPath("[].id").description("게임 Id"),
                    fieldWithPath("[].name").description("게임 이름"),
                    fieldWithPath("[].headCount").description("게임의 참가자 수"),
                    fieldWithPath("[].thumbnail").description("썸네일"))));
    }

    @DisplayName("단일 게임 이미지 조회")
    @Test
    void findGameImageById() throws Exception {
        mockMvc.perform(get("/api/games/1/images")
            .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.gameId").value(1L))
            .andExpect(jsonPath("$.image").value("https://static-cdn.jtvnw.net/ttv-boxart/League%20of%20Legends-1080x1436.jpg"))

            .andDo(document("read-game-image",
                responseFields(
                    fieldWithPath("gameId").description("게임 Id"),
                    fieldWithPath("image").description("이미지 URL"))));
    }

    @DisplayName("전체 게임 이미지 목록 조회")
    @Test
    void findGameImages() throws Exception {
        mockMvc.perform(get("/api/games/images")
            .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].gameId").value(1L))
            .andExpect(jsonPath("$[0].image").value("https://static-cdn.jtvnw.net/ttv-boxart/League%20of%20Legends-1080x1436.jpg"))
            .andExpect(jsonPath("$[1].gameId").value(2L))
            .andExpect(jsonPath("$[1].image").value("https://static-cdn.jtvnw.net/ttv-static/404_boxart-1080x1436.jpg"))
            .andExpect(jsonPath("$[2].gameId").value(3L))
            .andExpect(jsonPath("$[2].image").value("https://static-cdn.jtvnw.net/ttv-static/404_boxart-1080x1436.jpg"))

            .andDo(document("read-game-image",
                responseFields(fieldWithPath("[].gameId").description("게임 Id"),
                    fieldWithPath("[].image").description("이미지 URL"))
                )
            );
    }
}
