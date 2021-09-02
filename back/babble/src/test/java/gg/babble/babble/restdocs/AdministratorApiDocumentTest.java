package gg.babble.babble.restdocs;

import static org.assertj.core.api.Assertions.assertThat;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

public class AdministratorApiDocumentTest extends ApiDocumentTest {

    private Administrator localhost;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        super.setUp(webApplicationContext, restDocumentation);
        localhost = new Administrator("127.0.0.1", "localhost");
        administratorRepository.save(localhost);
    }

    @DisplayName("관리자 전체 조회한다.")
    @Test
    void findAllAdministrator() throws Exception {
        final Set<AdministratorResponse> expected = Collections.singleton(AdministratorResponse.from(localhost));

        final MvcResult mvcResult = 관리자_전체_조회()
            .andExpect(status().isOk())
            .andDo(document("read-administrators",
                responseFields(
                    fieldWithPath("[].id").description("관리자 ID"),
                    fieldWithPath("[].ip").description("관리자 IP 주소"),
                    fieldWithPath("[].name").description("관리자 이름")
                )
            )).andReturn();

        final HashSet<AdministratorResponse> responses = new HashSet<>(Arrays.asList(getResponseAs(mvcResult, AdministratorResponse[].class)));
        assertThat(responses).usingRecursiveComparison().isEqualTo(expected);
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
        final AdministratorResponse expected = new AdministratorResponse(null, ip, name);

        // then
        final MvcResult mvcResult = 관리자_IP_추가_됨(body)
            .andExpect(status().isCreated())
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
            )).andReturn();

        final AdministratorResponse response = getResponseAs(mvcResult, AdministratorResponse.class);

        assertThat(response).usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(expected);

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
