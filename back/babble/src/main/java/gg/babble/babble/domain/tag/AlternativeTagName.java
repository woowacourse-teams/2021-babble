package gg.babble.babble.domain.tag;

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
    private TagName name;

    @ManyToOne
    @JoinColumn(name = "tag_id")
    @NotNull(message = "게임은 Null 일 수 없습니다.")
    private Tag tag;

    public AlternativeTagName(final String name, final Tag tag) {
        this(null, name, tag);
    }

    public AlternativeTagName(final Long id, final String name, final Tag tag) {
        this.id = id;
        this.name = new TagName(name);

        setTag(tag);
    }

    public void setTag(final Tag tag) {
        this.tag = tag;

        if (tag.hasNotName(name)) {
            tag.addAlternativeName(this);
        }
    }
}
