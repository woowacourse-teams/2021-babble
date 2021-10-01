package gg.babble.babble.domain.slider;

import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Slider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private ResourceUrl resourceUrl;

    private int sortingIndex;

    public Slider(final String url) {
        this(null, new ResourceUrl(url), Integer.MAX_VALUE);
    }

    public Slider(final Long id, final ResourceUrl resourceUrl, final int sortingIndex) {
        this.id = id;
        this.resourceUrl = resourceUrl;
        this.sortingIndex = sortingIndex;
    }

    public String url() {
        return resourceUrl.getUrl();
    }

    public boolean isSameId(Long sliderId) {
        return id.equals(sliderId);
    }

    public void setSortingIndex(final int sortingIndex) {
        this.sortingIndex = sortingIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Slider slider = (Slider) o;
        return Objects.equals(id, slider.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
