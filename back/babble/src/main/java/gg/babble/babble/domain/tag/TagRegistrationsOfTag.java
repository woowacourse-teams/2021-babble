package gg.babble.babble.domain.tag;

import gg.babble.babble.domain.TagRegistration;
import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class TagRegistrationsOfTag {

    @OneToMany(mappedBy = "tag")
    private List<TagRegistration> tagRegistrations;

}
