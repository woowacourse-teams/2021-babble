package gg.babble.babble.dto.response;

import gg.babble.babble.domain.slider.Slider;
import gg.babble.babble.domain.slider.Sliders;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SliderResponse {

    private Long id;
    private String url;

    public static SliderResponse of(final Slider slider) {
        return new SliderResponse(slider.getId(), slider.url());
    }

    public static List<SliderResponse> of(final Sliders sliders) {
        return sliders.getValues()
            .stream()
            .sorted(Comparator.comparingInt(Slider::getSortingIndex))
            .map(SliderResponse::of)
            .collect(Collectors.toList());
    }
}
