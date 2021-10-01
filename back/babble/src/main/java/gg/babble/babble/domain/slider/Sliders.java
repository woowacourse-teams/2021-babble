package gg.babble.babble.domain.slider;

import gg.babble.babble.exception.BabbleIllegalArgumentException;
import gg.babble.babble.exception.BabbleNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public class Sliders {

    private final List<Slider> values;

    public Sliders(final List<Slider> values) {
        this.values = values;
    }

    private void validateExistIdsValue(final List<Long> ids) {
        List<Long> valueIds = ids();

        if (new HashSet<>(ids).size() != ids.size()) {
            throw new BabbleIllegalArgumentException("중복된 Slider ID가 포함되어 있습니다.");
        }

        if (!valueIds.containsAll(ids)) {
            throw new BabbleIllegalArgumentException("등록된 모든 Slider ID가 포함되어 있지 않습니다.");
        }

        if (ids.containsAll(valueIds)) {
            return;
        }

        throw new BabbleIllegalArgumentException("존재하지 않는 Slider ID가 입력되어 있습니다.");
    }

    private List<Long> ids() {
        return values.stream()
            .map(Slider::getId)
            .collect(Collectors.toList());
    }

    public void add(Slider slider) {
        slider.setSortingIndex(values.size());
        values.add(slider);
    }

    public Slider find(final Long sliderId) {
        return values.stream()
            .filter(slider -> slider.isSameId(sliderId))
            .findAny()
            .orElseThrow(() -> new BabbleNotFoundException(String.format("[%d]번 Slider ID는 존재하지 않습니다.", sliderId)));
    }

    public void changeOrder(final List<Long> ids) {
        validateExistIdsValue(ids);

        Map<Long, Slider> dictionary = listToMap(values);

        for (int i = 0; i < ids.size(); i++) {
            Slider slider = dictionary.get(ids.get(i));
            slider.setSortingIndex(i);
        }
    }

    private Map<Long, Slider> listToMap(final List<Slider> sliders) {
        Map<Long, Slider> dictionary = new HashMap<>();

        for (Slider slider : sliders) {
            dictionary.put(slider.getId(), slider);
        }

        return dictionary;
    }

    public void delete(Slider slider) {
        values.remove(slider);
        rearrange(slider.getSortingIndex());
    }

    private void rearrange(final int start) {
        for (int i = start; i < values.size(); i++) {
            values.get(i).setSortingIndex(i - 1);
        }
    }
}
