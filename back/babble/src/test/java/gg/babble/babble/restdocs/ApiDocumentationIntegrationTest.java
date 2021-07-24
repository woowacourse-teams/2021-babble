package gg.babble.babble.restdocs;

import com.fasterxml.jackson.databind.ObjectMapper;
import gg.babble.babble.ApplicationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
public class ApiDocumentationIntegrationTest extends ApplicationTest {

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext,
                      RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .alwaysDo(document("{method-name}", preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .build();
    }

    @Test
    public void createRoomTest() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("gameId", 1L);
        body.put("tags", Arrays.asList("실버", "2시간", "솔로랭크"));
        mockMvc.perform(post("/api/rooms")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.roomId").exists())
                .andExpect(jsonPath("$.createdDate").exists())
                .andExpect(jsonPath("$.game.id").value(1))
                .andExpect(jsonPath("$.game.name").exists())
                .andExpect(jsonPath("$.tags").isArray())
                .andExpect(jsonPath("$.tags", hasSize(3)))
                .andExpect(jsonPath("$.tags", hasItem("실버")))
                .andExpect(jsonPath("$.tags", hasItem("솔로랭크")))
                .andExpect(jsonPath("$.tags", hasItem("2시간")))
                .andDo(document("create-room",
                        requestFields(fieldWithPath("gameId").description("게임 Id"),
                                fieldWithPath("tags").description("태그 리스트")),
                        responseFields(fieldWithPath("roomId").description("방 Id"),
                                fieldWithPath("createdDate").description("방 생성 시각"),
                                fieldWithPath("game.id").description("게임 Id"),
                                fieldWithPath("game.name").description("게임 이름"),
                                fieldWithPath("tags").description("태그 리스트"))));
    }

    @Test
    public void readRoomTest() throws Exception {
        mockMvc.perform(get("/api/rooms/1")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roomId").exists())
                .andExpect(jsonPath("$.createdDate").exists())
                .andExpect(jsonPath("$.game.id").value(1))
                .andExpect(jsonPath("$.game.name").exists())
                .andExpect(jsonPath("$.tags").isArray())
                .andExpect(jsonPath("$.tags", hasSize(2)))
                .andExpect(jsonPath("$.tags", hasItem("실버")))
                .andExpect(jsonPath("$.tags", hasItem("2시간")))
                .andDo(document("read-room",
                        responseFields(fieldWithPath("roomId").description("방 Id"),
                                fieldWithPath("createdDate").description("방 생성 시각"),
                                fieldWithPath("game.id").description("게임 Id"),
                                fieldWithPath("game.name").description("게임 이름"),
                                fieldWithPath("tags").description("태그 리스트"))));
    }
}
