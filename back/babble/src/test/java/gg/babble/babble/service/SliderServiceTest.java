package gg.babble.babble.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import gg.babble.babble.ApplicationTest;
import gg.babble.babble.domain.slider.Slider;
import gg.babble.babble.dto.request.SliderOrderRequest;
import gg.babble.babble.dto.request.SliderRequest;
import gg.babble.babble.dto.response.SliderResponse;
import gg.babble.babble.exception.BabbleDuplicatedException;
import gg.babble.babble.exception.BabbleIllegalArgumentException;
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

    @DisplayName("중복된 아이디를 포함한 값으로 슬라이더 이미지를 순서를 변경한다.")
    @Test
    void updateWrongValue() {
        SliderOrderRequest request = new SliderOrderRequest(Arrays.asList(slider3.getId(), slider1.getId(), slider1.getId()));

        assertThatThrownBy(() -> sliderService.updateOrder(request)).isInstanceOf(BabbleDuplicatedException.class);
    }

    @DisplayName("슬라이더 이미지를 제거한다.")
    @Test
    void delete() {
        Slider slider4 = sliderRepository.save(new Slider("test4/path"));
        Slider slider5 = sliderRepository.save(new Slider("test5/path"));
        Slider slider6 = sliderRepository.save(new Slider("test6/path"));

        sliderService.delete(slider3.getId());
        List<SliderResponse> sliders = sliderService.findAll();

        for (SliderResponse slider : sliders) {
            System.out.println(slider.getId());
        }

        List<SliderResponse> expected = Arrays.asList(
            SliderResponse.from(slider1),
            SliderResponse.from(slider2),
            SliderResponse.from(slider4),
            SliderResponse.from(slider5),
            SliderResponse.from(slider6)
        );

        assertThat(sliders).usingRecursiveComparison().isEqualTo(expected);
    }

    @DisplayName("슬라이더 이미지를 제거하고 난 후 제거된 이미지 뒤에 있는 이미지의 인덱스가 새롭게 수정된다.")
    @Test
    void deleteAfterChangedIndex() {
        sliderService.delete(slider2.getId());

        Slider slider4 = new Slider("test4/path");

        sliderService.insert(new SliderRequest(slider4.url()));

        List<SliderResponse> slidersAfterDelete = sliderService.findAll();

        List<SliderResponse> expected = Arrays.asList(SliderResponse.from(slider1)
            , SliderResponse.from(slider3)
            , SliderResponse.from(slider4)
        );

        assertThat(slidersAfterDelete).usingRecursiveComparison().ignoringFields("id").isEqualTo(expected);
    }
}
