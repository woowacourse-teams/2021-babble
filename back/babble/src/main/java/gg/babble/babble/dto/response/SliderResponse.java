package gg.babble.babble.dto.response;

import gg.babble.babble.domain.slider.Slider;
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

    public static SliderResponse from(final Slider slider) {
        return new SliderResponse(slider.getId(), slider.url());
    }
}
