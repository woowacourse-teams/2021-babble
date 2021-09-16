package gg.babble.babble.domain.tag;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import gg.babble.babble.exception.BabbleDuplicatedException;
import gg.babble.babble.exception.BabbleLengthException;
import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class TagTest {

    @DisplayName("태그 이름은 1자 이상 20자 이하다.")
    @ParameterizedTest
    @ValueSource(strings = {"힝", "태그길이가스무자이다태그길이가스무자이다"})
    void nameLengthTest(String name) {
        assertThatCode(() -> new Tag(name)).doesNotThrowAnyException();
    }

    @DisplayName("태그 이름은 1자 이하면 예외가 발생한다.")
    @Test
    void nameLengthLessTest() {
        assertThatThrownBy(() -> new Tag("")).isInstanceOf(BabbleLengthException.class);
    }

    @DisplayName("태그 이름은 20자 이상이면 예외가 발생한다.")
    @Test
    void nameLengthOverTest() {
        assertThatThrownBy(() -> new Tag("태그길이가스무자이다태그길이가스무자이다하")).isInstanceOf(BabbleLengthException.class);
    }

    @DisplayName("단일 대체 이름 추가")
    @Test
    void addAlternativeName() {
        // given
        final Tag tag = new Tag(1L, "1시간");

        // when
        final AlternativeTagName alternativeName = new AlternativeTagName("1hour", tag);

        // then
        assertThat(tag.getAlternativeTagNames()).isEqualTo(new AlternativeTagNames(Collections.singletonList(alternativeName)));
    }

    @DisplayName("태그와 대체 이름 생성시 연관관계가 이어진다.")
    @Test
    void addAlternativeNames() {
        // given
        String tagName = "1시간";
        String name1 = "1Hour";
        String name2 = "한시간";

        // when
        Tag tag = new Tag(1L, tagName);

        AlternativeTagName alternativeTagName1 = new AlternativeTagName(1L, name1, tag);
        AlternativeTagName alternativeTagName2 = new AlternativeTagName(2L, name2, tag);

        // then
        assertThat(tag.getAlternativeTagNames().getNames()).containsExactly(name1, name2);
    }

    @DisplayName("대체 이름간 중복이 존재할 경우 예외가 발생한다.")
    @Test
    void duplicateAlternativeNamesException() {
        // given
        String tagName = "1시간";
        String name = "1Hour";

        // when
        Tag tag = new Tag(1L, tagName);
        AlternativeTagName alternativeTagName = new AlternativeTagName(1L, name, tag);

        // then
        assertThatThrownBy(() -> new AlternativeTagName(2L, name, tag)).isInstanceOf(BabbleDuplicatedException.class);
    }

    @DisplayName("태그 이름과 대체 이름이 중복되는 경우 예외가 발생한다.")
    @Test
    void duplicateAlternativeNameWithTagName() {
        // given
        String name = "1시간";

        // when
        Tag tag = new Tag(1L, name);

        // then
        assertThatThrownBy(() -> new AlternativeTagName(name, tag))
            .isExactlyInstanceOf(BabbleDuplicatedException.class);
    }

    @DisplayName("태그 정보를 변경한다.")
    @Test
    void updateTag() {
        Tag tag = new Tag(1L, "1시간");
        AlternativeTagName alternativeTagName = new AlternativeTagName("1HOUR", tag);

        String updateTagName = "2시간";
        String updateAlternativeTagName = "2HOUR";

        // when
        Tag target = new Tag(updateTagName);
        AlternativeTagName alternativeTargetTagName = new AlternativeTagName(updateAlternativeTagName, target);

        tag.update(target);

        // then
        assertThat(tag.getName()).isEqualTo(updateTagName);
        assertThat(tag.getAlternativeTagNames().getNames()).containsExactly(updateAlternativeTagName);
    }

    @DisplayName("단일 대체 이름 변경")
    @Test
    void changeAlternativeName() {
        // given
        final Tag tag = new Tag(1L, "1시간");
        final Tag tag2 = new Tag(2L, "2시간");
        final AlternativeTagName alternativeTagName = new AlternativeTagName("1hour", tag);
        // when
        alternativeTagName.setTag(tag2);
        // then
        assertThat(tag.hasName(alternativeTagName.getValue())).isFalse();
        assertThat(tag2.hasName(alternativeTagName.getValue())).isTrue();
        assertThat(alternativeTagName.getTag()).isEqualTo(tag2);
        assertThat(alternativeTagName.isDeleted()).isFalse();
    }

    @DisplayName("단일 대체 이름 삭제")
    @Test
    void delete() {
        // given
        final Tag tag = new Tag(1L, "1시간");
        final AlternativeTagName alternativeName = new AlternativeTagName("1hour", tag);
        // when
        tag.removeAlternativeName(alternativeName);
        // then
        assertThat(tag.hasName(alternativeName.getValue())).isFalse();
        assertThat(alternativeName.isDeleted()).isTrue();
    }
}
