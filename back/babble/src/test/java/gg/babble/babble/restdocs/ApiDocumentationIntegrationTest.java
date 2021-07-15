package gg.babble.babble.restdocs;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import gg.babble.babble.ApplicationTest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

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
        body.put("gameId", 1);
        body.put("hostId", 1);
        body.put("tags", Arrays.asList("실버", "2시간", "솔로랭크"));

        mockMvc.perform(post("/api/rooms")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(body))
            .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.roomId").exists())
            .andExpect(jsonPath("$.createData").exists())
            .andExpect(jsonPath("$.game.id").value(1))
            .andExpect(jsonPath("$.game.name").exists())
            .andExpect(jsonPath("$.host.id").value(1))
            .andExpect(jsonPath("$.host.name").exists())
            .andExpect(jsonPath("$.tags").isArray())
            .andExpect(jsonPath("$.tags", hasSize(3)))
            .andExpect(jsonPath("$.tags", hasItem("실버")))
            .andExpect(jsonPath("$.tags", hasItem("솔로랭크")))
            .andExpect(jsonPath("$.tags", hasItem("2시간")))
            .andDo(document("create-room",
                requestFields(fieldWithPath("gameId").description("게임 Id"),
                    fieldWithPath("hostId").description("호스트 Id"),
                    fieldWithPath("tags").description("태그 리스트")),
                responseFields(fieldWithPath("roomId").description("방 Id"),
                    fieldWithPath("createdDate").description("방 생성 시각"),
                    fieldWithPath("game.id").description("게임 Id"),
                    fieldWithPath("game.name").description("게임 이름"),
                    fieldWithPath("host.id").description("호스트 Id"),
                    fieldWithPath("host.name").description("호스트 닉네임"),
                    fieldWithPath("tags").description("태그 리스트"))));
    }
}
