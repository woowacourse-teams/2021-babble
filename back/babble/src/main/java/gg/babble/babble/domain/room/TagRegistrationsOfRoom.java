package gg.babble.babble.domain.room;

import gg.babble.babble.domain.tag.Tag;
import gg.babble.babble.domain.TagRegistration;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Embeddable
public class TagRegistrationsOfRoom {

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<TagRegistration> tagRegistrations;

    @Builder
    private TagRegistrationsOfRoom(Room room, List<Tag> tags) {
        this.tagRegistrations = tagRegistrationsFromTag(room, tags);
    }

    private List<TagRegistration> tagRegistrationsFromTag(Room room, List<Tag> tags) {
        return tags.stream()
            .map(tag -> TagRegistration.builder()
                .room(room)
                .tag(tag)
                .build())
            .collect(Collectors.toList());
    }

    public List<String> tagNames() {
        return tagRegistrations.stream()
            .map(tagRegistration -> tagRegistration.getTag().getName())
            .collect(Collectors.toList());
    }
}
