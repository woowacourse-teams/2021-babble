package gg.babble.babble.domain.tag;

import gg.babble.babble.domain.TagRegistration;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Embeddable
public class TagRegistrationsOfTag {

    @OneToMany(mappedBy = "tag", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<TagRegistration> tagRegistrations = new ArrayList<>();

    public boolean isNotEmpty() {
        return !tagRegistrations.isEmpty();
    }
}
