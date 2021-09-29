package gg.babble.babble.domain.slider;

import gg.babble.babble.dto.response.SliderResponse;
import gg.babble.babble.exception.BabbleIllegalArgumentException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class Sliders {

    private final List<Slider> values;

    public Sliders(final List<Slider> values) {
        this.values = values;
    }

    public void validateExistIdsValue(final List<Long> ids) {
        if (!ids().containsAll(ids)) {
            throw new BabbleIllegalArgumentException("등록된 모든 Slider Id 가 포함되어 있지 않습니다.");
        }

        if (ids.containsAll(ids())) {
            return;
        }

        throw new BabbleIllegalArgumentException("존재하지 않는 Slider Id 가 입력되어 있습니다.");
    }

    private List<Long> ids() {
        return values.stream()
            .map(Slider::getId)
            .collect(Collectors.toList());
    }

    public void sortValue(final List<Long> ids) {
        validateExistIdsValue(ids);

        final Map<Long, Slider> dictionary = listToMap(values);

        for (int i = 0; i < ids.size(); i++) {
            Slider slider = dictionary.get(ids.get(i));
            slider.setSortingIndex(i);
        }
    }

    private Map<Long, Slider> listToMap(final List<Slider> sliders) {
        final Map<Long, Slider> dictionary = new HashMap<>();

        for (Slider slider : sliders) {
            dictionary.put(slider.getId(), slider);
        }

        return dictionary;
    }

    public List<SliderResponse> toResponse() {
        return values.stream()
            .sorted(Comparator.comparingInt(Slider::getSortingIndex))
            .map(SliderResponse::from)
            .collect(Collectors.toList());
    }

    public void rearrange(final int start) {
        for (int i = start; i < values.size(); i++) {
            values.get(i - 1).setSortingIndex(i);
        }
    }
}
