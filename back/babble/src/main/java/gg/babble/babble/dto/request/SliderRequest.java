package gg.babble.babble.dto.request;

import gg.babble.babble.domain.slider.ResourceUrl;
import gg.babble.babble.domain.slider.Slider;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SliderRequest {

    @NotNull
    private String sliderUrl;

    public Slider toEntity() {
        return new Slider(sliderUrl);
    }
}
