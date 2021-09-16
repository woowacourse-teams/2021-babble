package gg.babble.babble.domain.game;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

import gg.babble.babble.exception.BabbleDuplicatedException;
import gg.babble.babble.exception.BabbleNotFoundException;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AlternativeGameNamesTest {

    private Game game;
    private AlternativeGameNames names;

    @BeforeEach
    void setUp() {
        game = new Game("디지투온", Collections.singletonList("화려한 이미지"));
        names = new AlternativeGameNames();
    }

    @DisplayName("대체 이름 추가")
    @Test
    void add() {
        final AlternativeGameName name = new AlternativeGameName("EZ2ON", game);
        names.add(name);

        assertThat(names.contains(name.getValue())).isTrue();
    }

    @DisplayName("이미 존재하는 이름 추가시 예외 처리")
    @Test
    void addSameName() {
        final AlternativeGameName name = new AlternativeGameName("EZ2ON", game);
        names.add(name);

        assertThatExceptionOfType(BabbleDuplicatedException.class).isThrownBy(() -> names.add(name));
    }

    @Test
    void remove() {
        final AlternativeGameName name = new AlternativeGameName("EZ2ON", game);
        names.add(name);

        names.remove(name);
        assertThat(names.contains(name.getValue())).isFalse();
    }

    @DisplayName("존재하지 않는 이름 제거")
    @Test
    void removeNotFoundName() {
        final AlternativeGameName name = new AlternativeGameName("EZ2ON", game);

        assertThatExceptionOfType(BabbleNotFoundException.class).isThrownBy(() -> names.remove(name));
    }
}