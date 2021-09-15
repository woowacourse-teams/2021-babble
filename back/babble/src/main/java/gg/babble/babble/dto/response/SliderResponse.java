package gg.babble.babble.dto.response;

import gg.babble.babble.domain.slider.Slider;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class SliderResponse {

    private final Long id;
    private final String url;

    public static SliderResponse from(final Slider slider) {
        return new SliderResponse(slider.getId(), slider.url());
    }
}
