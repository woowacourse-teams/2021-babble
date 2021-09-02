package gg.babble.babble.restdocs;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import gg.babble.babble.dto.response.UserResponse;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.test.web.servlet.MvcResult;
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
        UserResponse expected = new UserResponse(null, "fortune", "https://d2bidcnq0n74fu.cloudfront.net/img/users/profiles/profile57.png");
        Map<String, Object> body = new HashMap<>();
        body.put("nickname", "fortune");

        final MvcResult mvcResult = mockMvc.perform(post("/api/users")
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(body)).characterEncoding("utf-8"))
            .andExpect(status().isOk())
            .andDo(document("create-user",
                requestFields(fieldWithPath("nickname").description("닉네임")),
                responseFields(fieldWithPath("nickname").description("닉네임"),
                    fieldWithPath("id").description("유저 Id"),
                    fieldWithPath("avatar").description("아바타 URL")))
            ).andReturn();

        final UserResponse response = getResponseAs(mvcResult, UserResponse.class);
        assertThat(response).usingRecursiveComparison()
            .ignoringExpectedNullFields()
            .isEqualTo(expected);
    }
}
