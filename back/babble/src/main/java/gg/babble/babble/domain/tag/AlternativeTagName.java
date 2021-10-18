package gg.babble.babble.domain.tag;

import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Getter
@SQLDelete(sql = "UPDATE alternative_tag_name SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class AlternativeTagName {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private TagName value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    @NotNull(message = "게임은 Null 일 수 없습니다.")
    private Tag tag;

    private boolean deleted = false;

    public AlternativeTagName(final TagName tagName, final Tag tag) {
        this(null, tagName, tag);
    }

    public AlternativeTagName(final Long id, final TagName tagName, final Tag tag) {
        this.id = id;
        this.value = tagName;
        this.tag = tag;
    }

    public void delete() {
        deleted = true;
    }

    public boolean isSameName(final TagName name) {
        return value.equals(name);
    }

    public boolean isNotDeleted() {
        return !isDeleted();
    }

    public String getName() {
        return value.getValue();
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
