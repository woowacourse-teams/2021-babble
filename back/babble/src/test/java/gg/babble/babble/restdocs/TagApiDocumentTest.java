package gg.babble.babble.restdocs;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import gg.babble.babble.domain.tag.Tag;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.web.context.WebApplicationContext;

public class TagApiDocumentTest extends ApiDocumentTest {

    private final List<Tag> tags = new ArrayList<>();

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        super.setUp(webApplicationContext, restDocumentation);

        tags.add(tagRepository.save(new Tag("실버")));
        tags.add(tagRepository.save(new Tag("2시간")));
        tags.add(tagRepository.save(new Tag("솔로랭크")));
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
            .andExpect(jsonPath("$.[1].id").value(tags.get(1).getId()))
            .andExpect(jsonPath("$.[1].name").value(tags.get(1).getName()))
            .andExpect(jsonPath("$.[2].id").value(tags.get(2).getId()))
            .andExpect(jsonPath("$.[2].name").value(tags.get(2).getName()))

            .andDo(document("tags-get",
                responseFields(
                    fieldWithPath("[].id").description("태그 Id"),
                    fieldWithPath("[].name").description("태그 이름"))));
    }
}
