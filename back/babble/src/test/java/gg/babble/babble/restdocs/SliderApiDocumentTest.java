package gg.babble.babble.restdocs;

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
import gg.babble.babble.domain.slider.Slider;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.context.WebApplicationContext;

class SliderApiDocumentTest extends ApiDocumentTest {

    private Slider slider1;
    private Slider slider2;
    private Slider slider3;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) throws Exception {
        super.setUp(webApplicationContext, restDocumentation);

        slider1 = sliderRepository.save(new Slider("test/url/1"));
        slider2 = sliderRepository.save(new Slider("test/url/2"));
        slider3 = sliderRepository.save(new Slider("test/url/3"));

        administratorRepository.save(new Administrator("127.0.0.1", "localhost"));
    }

    @DisplayName("전체 슬라이더를 조회한다.")
    @Test
    void findAllSliders() throws Exception {
        mockMvc.perform(get("/api/sliders")
            .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(slider1.getId()))
            .andExpect(jsonPath("$[0].url").value(slider1.url()))
            .andExpect(jsonPath("$[1].id").value(slider2.getId()))
            .andExpect(jsonPath("$[1].url").value(slider2.url()))
            .andExpect(jsonPath("$[2].id").value(slider3.getId()))
            .andExpect(jsonPath("$[2].url").value(slider3.url()))

            .andDo(document("read-slider",
                responseFields(
                    fieldWithPath("[].id").description("슬라이더 Id"),
                    fieldWithPath("[].url").description("주소"))
            ));
    }

    @DisplayName("슬라이더를 추가한다.")
    @Test
    void insertSlider() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("sliderUrl", "testPath");
        mockMvc.perform(post("/api/sliders")
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(body)).characterEncoding("utf-8"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").isNumber())
            .andExpect(jsonPath("$.url").isString())

            .andDo(document("insert-slider",
                requestFields(fieldWithPath("sliderUrl").description("주소")),
                responseFields(fieldWithPath("id").description("슬라이더 Id"),
                    fieldWithPath("url").description("주소"))
            ));
    }

    @DisplayName("슬라이더 순서를 변경한다.")
    @Test
    void updateSlider() throws Exception {
        Map<String, Object> body = new HashMap<>();
        List<Long> ids = Arrays.asList(slider1.getId(), slider3.getId(), slider2.getId());
        body.put("ids", ids);
        mockMvc.perform(put("/api/sliders")
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(body)).characterEncoding("utf-8"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(slider1.getId()))
            .andExpect(jsonPath("$[0].url").value(slider1.url()))
            .andExpect(jsonPath("$[1].id").value(slider3.getId()))
            .andExpect(jsonPath("$[1].url").value(slider3.url()))
            .andExpect(jsonPath("$[2].id").value(slider2.getId()))
            .andExpect(jsonPath("$[2].url").value(slider2.url()))

            .andDo(document("update-slider-order",
                requestFields(fieldWithPath("ids").description("슬라이더 Id")),
                responseFields(fieldWithPath("[].id").description("슬라이더 Id"),
                    fieldWithPath("[].url").description("주소"))
            ));
    }

    @DisplayName("슬라이더를 제거한다.")
    @Test
    void deleteSlider() throws Exception {
        mockMvc.perform(delete("/api/sliders/" + slider1.getId())
            .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isNoContent())
            .andDo(document("delete-slider"));

        mockMvc.perform(get("/api/sliders")
            .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(slider2.getId()))
            .andExpect(jsonPath("$[0].url").value(slider2.url()))
            .andExpect(jsonPath("$[1].id").value(slider3.getId()))
            .andExpect(jsonPath("$[1].url").value(slider3.url()));
    }
}
