package gg.babble.babble.domain.slider;

import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class ResourceUrl {

    private String url;

    public ResourceUrl(String url) {
        this.url = url;
    }
}
