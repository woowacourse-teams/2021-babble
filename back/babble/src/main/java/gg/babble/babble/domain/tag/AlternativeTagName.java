package gg.babble.babble.domain.tag;

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
    //TODO Tag의 이름과 AlternativeTagName이름이 같은 검증을 하도록 리팩토링
    @NotNull(message = "대안 이름은 Null 일 수 없습니다.")
    private String name;
    @ManyToOne
    @JoinColumn(name = "tag_id")
    @NotNull(message = "게임은 Null 일 수 없습니다.")
    private Tag tag;

    public AlternativeTagName(final String name, final Tag tag) {
        this(null, name, tag);
    }

    public AlternativeTagName(final Long id, final String name, final Tag tag) {
        this.id = id;
        this.name = name;

        setTag(tag);
    }

    public void setTag(final Tag tag) {
        this.tag = tag;

        if (tag.hasNotName(name)) {
            tag.addAlternativeName(this);
        }
    }
}
