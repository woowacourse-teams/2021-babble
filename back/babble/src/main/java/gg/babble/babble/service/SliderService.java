package gg.babble.babble.service;

import gg.babble.babble.domain.repository.SliderRepository;
import gg.babble.babble.domain.slider.Slider;
import gg.babble.babble.dto.request.SliderOrderRequest;
import gg.babble.babble.dto.request.SliderRequest;
import gg.babble.babble.dto.response.SliderResponse;
import gg.babble.babble.exception.BabbleNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        Slider slider = sliderRepository.save(request.toEntity());
        return SliderResponse.from(slider);
    }

    public List<SliderResponse> findAll() {
        List<Slider> sliders = sliderRepository.findAll();
        sliders.sort(Comparator.comparingInt(Slider::getSortingIndex));

        return sliders.stream()
            .map(SliderResponse::from)
            .collect(Collectors.toList());
    }

    @Transactional
    public List<SliderResponse> updateOrder(SliderOrderRequest request) {
        Map<Long, Slider> dictionary = listToMap(sliderRepository.findAll());
        sort(dictionary, request.getIds());

        List<Slider> changed = new ArrayList<>(dictionary.values());
        sliderRepository.saveAll(changed);

        return changed.stream()
            .sorted(Comparator.comparingInt(Slider::getSortingIndex))
            .map(SliderResponse::from)
            .collect(Collectors.toList());
    }

    private Map<Long, Slider> listToMap(List<Slider> sliders) {
        Map<Long, Slider> dictionary = new HashMap<>();

        for (Slider slider : sliders) {
            dictionary.put(slider.getId(), slider);
        }

        return dictionary;
    }

    private void sort(Map<Long, Slider> dictionary, List<Long> ids) {
        for (int i = 0; i < ids.size(); i++) {
            Slider slider = dictionary.get(ids.get(i));
            slider.setSortingIndex(i);
        }
    }

    @Transactional
    public void delete(Long sliderId) {
        Slider slider = sliderRepository.findById(sliderId).orElseThrow(BabbleNotFoundException::new);
        sliderRepository.delete(slider);
    }
}
