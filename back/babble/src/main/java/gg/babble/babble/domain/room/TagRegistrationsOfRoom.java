package gg.babble.babble.domain.room;

import gg.babble.babble.domain.TagRegistration;
import gg.babble.babble.domain.tag.Tag;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Embeddable
public class TagRegistrationsOfRoom {

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TagRegistration> tagRegistrations;

    public TagRegistrationsOfRoom(final Room room, final List<Tag> tags) {
        this.tagRegistrations = tagRegistrationsFromTag(room, tags);
    }

    private List<TagRegistration> tagRegistrationsFromTag(final Room room, final List<Tag> tags) {
        return tags.stream()
            .map(tag -> new TagRegistration(room, tag))
            .collect(Collectors.toList());
    }

    public List<Tag> tags() {
        return tagRegistrations.stream()
            .map(TagRegistration::getTag)
            .collect(Collectors.toList());
    }
}
