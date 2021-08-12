package gg.babble.babble.restdocs;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.context.WebApplicationContext;

public class UserApiDocumentTest extends ApiDocumentTest {

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        super.setUp(webApplicationContext, restDocumentation);
    }

    @DisplayName("유저를 생성한다.")
    @Test
    void createUser() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("nickname", "fortune");

        mockMvc.perform(post("/api/users")
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(body)).characterEncoding("utf-8"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nickname").value("fortune"))
            .andExpect(jsonPath("$.id").isNumber())
            .andExpect(jsonPath("$.avatar").value("https://bucket-babble-front.s3.ap-northeast-2.amazonaws.com/img/users/profiles/profile57.png"))

            .andDo(document("create-user",
                requestFields(fieldWithPath("nickname").description("닉네임")),
                responseFields(fieldWithPath("nickname").description("닉네임"),
                    fieldWithPath("id").description("유저 Id"),
                    fieldWithPath("avatar").description("아바타 URL"))));
    }
}
