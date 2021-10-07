package gg.babble.babble.restdocs;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
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
import gg.babble.babble.domain.tag.Tag;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.context.WebApplicationContext;

public class TagApiDocumentTest extends ApiDocumentTest {

    private static final String ALTERNATIVE_NAME1 = "silver";
    private static final String ALTERNATIVE_NAME2 = "실딱이";
    private static final String ALTERNATIVE_NAME3 = "2hours";
    private final List<Tag> tags = new ArrayList<>();

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) throws Exception {
        super.setUp(webApplicationContext, restDocumentation);

        tags.add(tagRepository.save(new Tag("실버")));
        tags.add(tagRepository.save(new Tag("2시간")));
        tags.add(tagRepository.save(new Tag("솔로랭크")));

        tags.get(0).addNames(Arrays.asList(ALTERNATIVE_NAME1, ALTERNATIVE_NAME2));
        tags.get(1).addNames(Collections.singletonList(ALTERNATIVE_NAME3));
    }

    @DisplayName("전체 태그를 가져오는데 성공하면, 200응답 코드와 전체 태그를 가져온다.")
    @Test
    public void tagsGetTest() throws Exception {
        mockMvc.perform(get("/api/tags")
                .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").exists())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$", hasSize(3)))
            .andExpect(jsonPath("$.[0].id").value(tags.get(0).getId()))
            .andExpect(jsonPath("$.[0].name").value(tags.get(0).getName()))
            .andExpect(jsonPath("$.[0].alternativeNames").value(hasSize(2)))
            .andExpect(jsonPath("$.[0].alternativeNames").value(Matchers.containsInAnyOrder(ALTERNATIVE_NAME1, ALTERNATIVE_NAME2)))
            .andExpect(jsonPath("$.[1].id").value(tags.get(1).getId()))
            .andExpect(jsonPath("$.[1].name").value(tags.get(1).getName()))
            .andExpect(jsonPath("$.[1].alternativeNames").value(hasSize(1)))
            .andExpect(jsonPath("$.[1].alternativeNames").value(Matchers.containsInAnyOrder(ALTERNATIVE_NAME3)))
            .andExpect(jsonPath("$.[2].id").value(tags.get(2).getId()))
            .andExpect(jsonPath("$.[2].name").value(tags.get(2).getName()))
            .andExpect(jsonPath("$.[2].alternativeNames").value(hasSize(0)))

            .andDo(document("tags-get",
                responseFields(
                    fieldWithPath("[].id").description("태그 Id"),
                    fieldWithPath("[].name").description("태그 이름"),
                    fieldWithPath("[].alternativeNames").description("대체 이름"))));
    }

    @DisplayName("태그를 추가한다.")
    @Test
    void createTag() throws Exception {
        administratorRepository.save(new Administrator("127.0.0.1", "localhost"));

        String tagName = "ROOT";
        List<String> alternativeNames = Arrays.asList("blood lord", "darkness of mine", "i'm necessary one");

        Map<String, Object> body = new HashMap<>();
        body.put("name", tagName);
        body.put("alternativeNames", alternativeNames);

        mockMvc.perform(post("/api/tags")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(body)).characterEncoding("utf-8"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").isNumber())
            .andExpect(jsonPath("$.name").value(tagName))
            .andExpect(jsonPath("$.alternativeNames.length()").value(alternativeNames.size()))
            .andExpect(jsonPath("$.alternativeNames").value(contains(alternativeNames.toArray())))

            .andDo(document("insert-tag",
                requestFields(
                    fieldWithPath("name").description("태그 이름"),
                    fieldWithPath("alternativeNames").description("대체 이름")
                ),
                responseFields(
                    fieldWithPath("id").description("태그 ID"),
                    fieldWithPath("name").description("태그 이름"),
                    fieldWithPath("alternativeNames").description("대체 이름")
                )
            ));
    }

    @DisplayName("등록되어 있지 않은 관리자의 경우 태그를 추가할 수 없다.")
    @Test
    void createTagWithInvalidAdministrator() throws Exception {
        String tagName = "ROOT";
        List<String> alternativeNames = Arrays.asList("blood lord", "darkness of mine", "i'm necessary one");

        Map<String, Object> body = new HashMap<>();
        body.put("name", tagName);
        body.put("alternativeNames", alternativeNames);

        mockMvc.perform(post("/api/tags")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(body)).characterEncoding("utf-8"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isUnauthorized());
    }

    @DisplayName("태그를 수정한다.")
    @Test
    void updateTag() throws Exception {
        administratorRepository.save(new Administrator("127.0.0.1", "localhost"));

        String tagName = "SILVER ROOT";
        List<String> alternativeNames = Arrays.asList("ultra", "amazing", "necessary root");

        Map<String, Object> body = new HashMap<>();
        body.put("name", tagName);
        body.put("alternativeNames", alternativeNames);

        mockMvc.perform(put("/api/tags/" + tags.get(0).getId())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(body)).characterEncoding("utf-8"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").isNumber())
            .andExpect(jsonPath("$.name").value(tagName))
            .andExpect(jsonPath("$.alternativeNames.length()").value(alternativeNames.size()))
            .andExpect(jsonPath("$.alternativeNames").value(contains(alternativeNames.toArray())))

            .andDo(document("update-tag",
                requestFields(
                    fieldWithPath("name").description("태그 이름"),
                    fieldWithPath("alternativeNames").description("대체 이름")
                ),
                responseFields(
                    fieldWithPath("id").description("태그 ID"),
                    fieldWithPath("name").description("태그 이름"),
                    fieldWithPath("alternativeNames").description("대체 이름")
                )
            ));
    }

    @DisplayName("등록되어 있지 않은 관리자의 경우 태그를 수정할 수 없다.")
    @Test
    void updateTagWithInvalidAdministrator() throws Exception {
        String tagName = "SILVER ROOT";
        List<String> alternativeNames = Arrays.asList("ultra", "amazing", "necessary root");

        Map<String, Object> body = new HashMap<>();
        body.put("name", tagName);
        body.put("alternativeNames", alternativeNames);

        mockMvc.perform(put("/api/tags/" + tags.get(0).getId())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(body)).characterEncoding("utf-8"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isUnauthorized());
    }

    @DisplayName("태그를 삭제한다")
    @Test
    void deleteTag() throws Exception {
        administratorRepository.save(new Administrator("127.0.0.1", "localhost"));

        mockMvc.perform(delete("/api/tags/" + tags.get(0).getId()).
                accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isNoContent())
            .andDo(document("delete-game"));

        mockMvc.perform(get("/api/tags")
                .accept(MediaType.APPLICATION_JSON_VALUE))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].id").value(not(tags.get(0).getId())))
            .andExpect(jsonPath("$[1].id").value(not(tags.get(0).getId())));
    }

    @DisplayName("등록되어 있지 않은 관리자의 경우 게임을 삭제할 수 없다.")
    @Test
    void deleteTagWithInvalidAdministrator() throws Exception {
        mockMvc.perform(delete("/api/tags/" + tags.get(0).getId()).
                accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isUnauthorized());
    }
}
