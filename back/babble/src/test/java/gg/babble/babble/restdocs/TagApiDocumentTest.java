package gg.babble.babble.restdocs;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import gg.babble.babble.domain.tag.Tag;
import gg.babble.babble.dto.response.TagResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.test.web.servlet.MvcResult;
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
        final List<TagResponse> expected = tags.stream()
            .map(TagResponse::from)
            .collect(Collectors.toList());
        final MvcResult mvcResult = mockMvc.perform(get("/api/tags")
            .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andDo(document("tags-get",
                responseFields(
                    fieldWithPath("[].id").description("태그 Id"),
                    fieldWithPath("[].name").description("태그 이름")))
            ).andReturn();

        final List<TagResponse> responses = Arrays.asList(getResponseAs(mvcResult, TagResponse[].class));
        assertThat(responses).usingRecursiveComparison().isEqualTo(expected);
    }
}
