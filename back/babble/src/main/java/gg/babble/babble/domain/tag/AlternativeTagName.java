package gg.babble.babble.domain.tag;

import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class AlternativeTagName {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private TagName value;

    @ManyToOne
    @JoinColumn(name = "tag_id")
    @NotNull(message = "게임은 Null 일 수 없습니다.")
    private Tag tag;

    public AlternativeTagName(final String value, final Tag tag) {
        this(null, value, tag);
    }

    public AlternativeTagName(final Long id, final String value, final Tag tag) {
        this.id = id;
        this.value = new TagName(value);

        setTag(tag);
    }

    public void setTag(final Tag tag) {
        this.tag = tag;

        if (tag.hasNotName(value)) {
            tag.addAlternativeName(this);
        }
    }

    public boolean isSameName(final TagName name) {
        return value.equals(name);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AlternativeTagName that = (AlternativeTagName) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
