package gg.babble.babble.service;

import gg.babble.babble.domain.repository.SliderRepository;
import gg.babble.babble.domain.slider.Slider;
import gg.babble.babble.domain.slider.Sliders;
import gg.babble.babble.dto.request.SliderOrderRequest;
import gg.babble.babble.dto.request.SliderRequest;
import gg.babble.babble.dto.response.SliderResponse;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
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
        Slider slider = request.toEntity();
        sliders.add(slider);

        Slider savedSlider = sliderRepository.save(slider);

        return SliderResponse.from(savedSlider);
    }

    public List<SliderResponse> findAll() {
        final Sliders sliders = new Sliders(sliderRepository.findAll());

        final List<Slider> result = sliders.getValues();

        return toResponse(result);
    }

    @Transactional
    public List<SliderResponse> updateOrder(final SliderOrderRequest request) {
        final Sliders sliders = new Sliders(sliderRepository.findAll());
        final List<Long> ids = request.getIds();

        final List<Slider> result = sliders.sortValue(ids);

        return toResponse(result);
    }

    @Transactional
    public void delete(final Long sliderId) {
        final Sliders sliders = new Sliders(sliderRepository.findAll());
        Slider slider = sliders.find(sliderId);

        sliderRepository.delete(slider);

        sliders.rearrange(slider.getSortingIndex());
    }

    private List<SliderResponse> toResponse(List<Slider> result) {
        return result.stream()
            .sorted(Comparator.comparingInt(Slider::getSortingIndex))
            .map(SliderResponse::from)
            .collect(Collectors.toList());
    }
}
