package gg.babble.babble.restdocs;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import gg.babble.babble.domain.admin.Administrator;
import gg.babble.babble.dto.response.AdministratorResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

public class AdministratorApiDocumentTest extends ApiDocumentTest {

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        super.setUp(webApplicationContext, restDocumentation);
        administratorRepository.save(new Administrator("127.0.0.1", "localhost"));
    }

    @DisplayName("관리자 전체 조회한다.")
    @Test
    void findAllAdministrator() throws Exception {
        관리자_전체_조회()
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.[0].id").isNumber())
            .andExpect(jsonPath("$.[0].ip").value("127.0.0.1"))
            .andExpect(jsonPath("$.[0].name").value("localhost"))

            .andDo(document("read-administrators",
                responseFields(
                    fieldWithPath("[].id").description("관리자 ID"),
                    fieldWithPath("[].ip").description("관리자 IP 주소"),
                    fieldWithPath("[].name").description("관리자 이름")
                )
            ));
    }

    private ResultActions 관리자_전체_조회() throws Exception {
        return mockMvc.perform(get("/api/admins")
            .accept(MediaType.APPLICATION_JSON_VALUE));
    }

    @DisplayName("관리자 IP를 추가한다.")
    @Test
    void createAdministrator() throws Exception {
        // given
        String ip = "111.111.11.11";
        String name = "포츈집";

        // when
        Map<String, Object> body = new HashMap<>();
        body.put("ip", ip);
        body.put("name", name);

        // then
        관리자_IP_추가_됨(body)
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").isNumber())
            .andExpect(jsonPath("$.ip").value(ip))
            .andExpect(jsonPath("$.name").value(name))

            .andDo(document("insert-administrator",
                requestFields(
                    fieldWithPath("ip").description("관리자 IP 주소"),
                    fieldWithPath("name").description("관리자 이름")
                ),
                responseFields(
                    fieldWithPath("id").description("관리자 ID"),
                    fieldWithPath("ip").description("관리자 IP 주소"),
                    fieldWithPath("name").description("관리자 이름")
                )
            ));

        관리자_전체_조회()
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)));
    }

    private ResultActions 관리자_IP_추가_됨(final Map<String, Object> body) throws Exception {
        return mockMvc.perform(post("/api/admins")
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(body)).characterEncoding("utf-8"));
    }

    @DisplayName("관리자 IP를 삭제한다.")
    @Test
    void deleteAdministrator() throws Exception {
        String response = 관리자_전체_조회().andReturn().getResponse().getContentAsString();
        Long idToDelete = objectMapper.readValue(
            response, new TypeReference<List<AdministratorResponse>>() {
            }).get(0).getId();

        mockMvc.perform(delete("/api/admins/" + idToDelete)
                .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isNoContent())
            .andDo(document("delete-administrators"));

        관리자_전체_조회().andExpect(status().isUnauthorized());
    }
}