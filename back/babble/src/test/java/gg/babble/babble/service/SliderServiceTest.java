package gg.babble.babble.service;

import static org.assertj.core.api.Assertions.assertThat;

import gg.babble.babble.ApplicationTest;
import gg.babble.babble.domain.slider.Slider;
import gg.babble.babble.dto.request.SliderOrderRequest;
import gg.babble.babble.dto.request.SliderRequest;
import gg.babble.babble.dto.response.SliderResponse;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class SliderServiceTest extends ApplicationTest {

    private Slider slider1;
    private Slider slider2;
    private Slider slider3;

    @Autowired
    private SliderService sliderService;

    @BeforeEach
    public void setUp() {
        slider1 = sliderRepository.save(new Slider("test1/path"));
        slider2 = sliderRepository.save(new Slider("test2/path"));
        slider3 = sliderRepository.save(new Slider("test3/path"));
    }

    @DisplayName("슬라이더 이미지를 추가한다.")
    @Test
    void insert() {
        SliderRequest request = new SliderRequest("test/path");
        SliderResponse response = sliderService.insert(request);

        SliderResponse expected = SliderResponse.from(new Slider("test/path"));

        assertThat(response).usingRecursiveComparison().ignoringFields("id").isEqualTo(expected);
    }

    @DisplayName("전체 슬라이더 이미지를 가져온다.")
    @Test
    void findAll() {
        List<SliderResponse> sliders = sliderService.findAll();
        List<SliderResponse> expected = Arrays.asList(
            SliderResponse.from(slider1),
            SliderResponse.from(slider2),
            SliderResponse.from(slider3)
        );

        assertThat(sliders).usingRecursiveComparison().isEqualTo(expected);
    }

    @DisplayName("슬라이더 이미지를 순서를 변경한다.")
    @Test
    void update() {
        SliderOrderRequest request = new SliderOrderRequest(Arrays.asList(slider3.getId(), slider1.getId(), slider2.getId()));
        List<SliderResponse> responses = sliderService.updateOrder(request);

        List<SliderResponse> expected = Arrays.asList(SliderResponse.from(slider3)
            , SliderResponse.from(slider1)
            , SliderResponse.from(slider2)
        );

        assertThat(responses).usingRecursiveComparison().ignoringFields("id").isEqualTo(expected);
    }

    @DisplayName("슬라이더 이미지를 제거한다.")
    @Test
    void delete() {
        sliderService.delete(slider2.getId());
        List<SliderResponse> sliders = sliderService.findAll();
        List<SliderResponse> expected = Arrays.asList(
            SliderResponse.from(slider1),
            SliderResponse.from(slider3)
        );

        assertThat(sliders).usingRecursiveComparison().isEqualTo(expected);
    }
}
