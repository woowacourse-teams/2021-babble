package gg.babble.babble.domain.tag;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import gg.babble.babble.exception.BabbleDuplicatedException;
import gg.babble.babble.exception.BabbleLengthException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
        Tag tag = new Tag(1L, "1시간");

        // when
        AlternativeTagName alternativeName = new AlternativeTagName(new TagName("1hour"), tag);

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

        AlternativeTagName alternativeTagName1 = new AlternativeTagName(1L, new TagName(name1), tag);
        AlternativeTagName alternativeTagName2 = new AlternativeTagName(2L, new TagName(name2), tag);

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
        AlternativeTagName alternativeTagName = new AlternativeTagName(1L, new TagName(name), tag);

        // then
        assertThatThrownBy(() -> new AlternativeTagName(2L, new TagName(name), tag)).isInstanceOf(BabbleDuplicatedException.class);
    }

    @DisplayName("태그 이름과 대체 이름이 중복되는 경우 예외가 발생한다.")
    @Test
    void duplicateAlternativeNameWithTagName() {
        // given
        String name = "1시간";

        // when
        Tag tag = new Tag(1L, name);

        // then
        assertThatThrownBy(() -> new AlternativeTagName(new TagName(name), tag))
            .isExactlyInstanceOf(BabbleDuplicatedException.class);
    }

    @DisplayName("태그 정보를 변경한다.")
    @Test
    void updateTag() {
        Tag tag = new Tag(1L, "1시간");
        tag.addNames(Collections.singletonList("1HOUR"));

        String updateTagName = "2시간";
        String updateAlternativeTagName = "2HOUR";

        // when
        Tag target = new Tag(updateTagName);
        target.addNames(Collections.singletonList(updateAlternativeTagName));

        tag.update(target);

        // then
        assertThat(tag.getName()).isEqualTo(updateTagName);
        assertThat(tag.getAlternativeTagNames().getNames()).containsExactly(updateAlternativeTagName);
    }

    @DisplayName("단일 대체 이름 삭제")
    @Test
    void deleteAlternativeName() {
        // given
        Tag tag = new Tag(1L, "1시간");
        AlternativeTagName alternativeName = new AlternativeTagName(new TagName("1hour"), tag);

        // when
        assertThat(tag.isDeleted()).isFalse();
        assertThat(alternativeName.isDeleted()).isFalse();

        tag.removeAlternativeName(alternativeName);

        // then
        assertThat(tag.hasName(alternativeName.getValue())).isFalse();
        assertThat(alternativeName.isDeleted()).isTrue();
    }

    @DisplayName("태그를 삭제할 경우 대체 이름도 모두 삭제된다.")
    @Test
    void deleteTag() {
        // given
        Tag tag = new Tag(1L, "1시간");
        AlternativeTagName alternativeName = new AlternativeTagName(new TagName("1hour"), tag);

        // when
        assertThat(tag.isDeleted()).isFalse();
        assertThat(alternativeName.isDeleted()).isFalse();

        tag.delete();

        // then
        assertThat(tag.isDeleted()).isTrue();
        assertThat(alternativeName.isDeleted()).isTrue();
    }

    @DisplayName("복수 개의 이름 추가")
    @Test
    void addNames() {
        // given
        Tag tag = new Tag(1L, "1시간");

        // when
        List<String> alternativeNames = Arrays.asList("1hour", "1時間");
        tag.addNames(alternativeNames);

        // then
        assertThat(tag.getAlternativeNames()).hasSameSizeAs(alternativeNames).containsAll(alternativeNames);
    }

    @DisplayName("복수 개의 이름 추가시 이미 존재하는 이름이면 예외 처리")
    @Test
    void invalidAddNames() {
        // given
        Tag tag = new Tag(1L, "1시간");

        // then
        List<String> alternativeNames = Arrays.asList("1hour", "1시간");
        assertThatThrownBy(() -> tag.addNames(alternativeNames)).isExactlyInstanceOf(BabbleDuplicatedException.class);
    }

    @DisplayName("복수 개의 이름 추가시 중복된 이름이 있으면 예외 처리")
    @Test
    void addDuplicatedNames() {
        // given
        Tag tag = new Tag(1L, "1시간");

        // when
        List<String> alternativeNames = Arrays.asList("1hour", "1hour", "1時間");
        assertThatThrownBy(() -> tag.addNames(alternativeNames)).isExactlyInstanceOf(BabbleDuplicatedException.class);
    }
}
