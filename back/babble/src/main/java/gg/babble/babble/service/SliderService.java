package gg.babble.babble.service;

import gg.babble.babble.domain.repository.SliderRepository;
import gg.babble.babble.domain.slider.Slider;
import gg.babble.babble.domain.slider.Sliders;
import gg.babble.babble.dto.request.SliderOrderRequest;
import gg.babble.babble.dto.request.SliderRequest;
import gg.babble.babble.dto.response.SliderResponse;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class SliderService {

    private final SliderRepository sliderRepository;

    public SliderService(final SliderRepository sliderRepository) {
        this.sliderRepository = sliderRepository;
    }

    @Transactional
    public SliderResponse insert(final SliderRequest request) {
        Sliders sliders = new Sliders(sliderRepository.findAll());
        Slider slider = sliderRepository.save(request.toEntity());
        sliders.add(slider);

        return SliderResponse.of(slider);
    }

    public List<SliderResponse> findAll() {
        Sliders sliders = new Sliders(sliderRepository.findAll());

        return SliderResponse.of(sliders);
    }

    @Transactional
    public List<SliderResponse> updateOrder(final SliderOrderRequest request) {
        Sliders sliders = new Sliders(sliderRepository.findAll());

        sliders.changeOrder(request.getIds());

        return SliderResponse.of(sliders);
    }

    @Transactional
    public void delete(final Long sliderId) {
        Sliders sliders = new Sliders(sliderRepository.findAll());
        Slider slider = sliders.find(sliderId);

        sliders.delete(slider);

        sliderRepository.delete(slider);
    }
}
