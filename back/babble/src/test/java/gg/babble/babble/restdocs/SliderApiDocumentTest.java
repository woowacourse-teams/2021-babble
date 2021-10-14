package gg.babble.babble.restdocs;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import gg.babble.babble.dto.response.SliderResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.RestDocumentationContextProvider;

class SliderApiDocumentTest extends AcceptanceTest {

    private List<SliderResponse> sliders;

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) throws Exception {
        super.setUp(restDocumentation);
        sliders = new ArrayList<>();

        localhost_관리자가_추가_됨();
        sliders.add(슬라이더_추가_됨("test/url/1"));
        sliders.add(슬라이더_추가_됨("test/url/2"));
        sliders.add(슬라이더_추가_됨("test/url/3"));
        localhost_관리자가_제거_됨();
    }

    private SliderResponse 슬라이더_추가_됨(final String url) {
        Map<String, Object> body = new HashMap<>();
        body.put("sliderUrl", "testPath");

        return given().body(body)
            .when().post("/api/sliders")
            .then().statusCode(HttpStatus.OK.value())
            .extract().body().as(SliderResponse.class);
    }


    @DisplayName("전체 슬라이더를 조회한다.")
    @Test
    void findAllSliders() {
        List<SliderResponse> responses = given().filter(document("read-sliders",
                responseFields(
                    fieldWithPath("[].id").description("슬라이더 Id"),
                    fieldWithPath("[].url").description("주소"))))
            .when().get("/api/sliders")
            .then().statusCode(HttpStatus.OK.value())
            .extract().jsonPath().getList(".", SliderResponse.class);

        assertThatSliderOrder(responses, sliders);
    }

    private void assertThatSliderOrder(final List<SliderResponse> actual, final List<SliderResponse> expected) {
        assertThat(actual).hasSameSizeAs(expected);

        for (int i = 0; i < expected.size(); i++) {
            SliderResponse expectedSlider = expected.get(i);
            SliderResponse actualSlider = actual.get(i);

            assertThat(actualSlider.getId()).isEqualTo(expectedSlider.getId());
            assertThat(actualSlider.getUrl()).isEqualTo(expectedSlider.getUrl());
        }
    }

    @DisplayName("슬라이더를 추가한다.")
    @Test
    void insertSlider() {
        localhost_관리자가_추가_됨();
        Map<String, Object> body = new HashMap<>();
        body.put("sliderUrl", "testPath");

        SliderResponse response = given().body(body)
            .filter(document("create-slider",
                requestFields(fieldWithPath("sliderUrl").description("주소")),
                responseFields(fieldWithPath("id").description("슬라이더 Id"),
                    fieldWithPath("url").description("주소"))))
            .when().post("/api/sliders")
            .then().statusCode(HttpStatus.OK.value())
            .extract().as(SliderResponse.class);

        assertThat(response.getId()).isNotNull();
        assertThat(response.getUrl()).isEqualTo("testPath");
    }

    @DisplayName("허용하지 않은 Ip는 슬라이더를 추가할 수 없다.")
    @Test
    void insertSliderWithInvalidIp() {
        Map<String, Object> body = new HashMap<>();
        body.put("sliderUrl", "testPath");

        given().body(body)
            .when().post("/api/sliders")
            .then().statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("슬라이더 순서를 변경한다.")
    @Test
    void updateSlider() {
        localhost_관리자가_추가_됨();
        Map<String, Object> body = new HashMap<>();
        List<Long> ids = Arrays.asList(sliders.get(0).getId(), sliders.get(2).getId(), sliders.get(1).getId());
        body.put("ids", ids);

        List<SliderResponse> responses = given().body(body)
            .filter(document("update-slider",
                requestFields(fieldWithPath("ids").description("슬라이더 Id")),
                responseFields(fieldWithPath("[].id").description("슬라이더 Id"),
                    fieldWithPath("[].url").description("주소"))))
            .when().put("/api/sliders")
            .then().statusCode(HttpStatus.OK.value())
            .extract().jsonPath().getList(".", SliderResponse.class);

        assertThatSliderOrder(responses, Arrays.asList(sliders.get(0), sliders.get(2), sliders.get(1)));
    }

    @DisplayName("허용하지 않는 Ip는 슬라이더 순서를 변경할 수 없다.")
    @Test
    void updateSliderWithInvalidIp() {
        Map<String, Object> body = new HashMap<>();
        List<Long> ids = Arrays.asList(sliders.get(0).getId(), sliders.get(2).getId(), sliders.get(1).getId());
        body.put("ids", ids);

        given().body(body)
            .when().put("/api/sliders")
            .then().statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("슬라이더를 제거한다.")
    @Test
    void deleteSlider() throws Exception {
        localhost_관리자가_추가_됨();

        given().filter(document("delete-slider"))
            .when().delete("/api/sliders/{id}", sliders.get(0).getId())
            .then().statusCode(HttpStatus.NO_CONTENT.value());

        List<SliderResponse> responses = 슬라이더가_조회_됨();
        assertThatSliderOrder(responses, Arrays.asList(sliders.get(1), sliders.get(2)));
    }

    private List<SliderResponse> 슬라이더가_조회_됨() {
        return given()
            .when().get("/api/sliders")
            .then().statusCode(HttpStatus.OK.value())
            .extract().jsonPath().getList(".", SliderResponse.class);
    }

    @DisplayName("허용하지 않는 Ip는 슬라이더 순서를 변경할 수 없다.")
    @Test
    void deleteSliderWithInvalidIp() {
        given()
            .when().delete("/api/sliders/{id}", sliders.get(0).getId())
            .then().statusCode(HttpStatus.UNAUTHORIZED.value());
    }
}
