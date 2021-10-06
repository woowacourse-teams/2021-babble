package gg.babble.babble.domain.repository;

import gg.babble.babble.domain.tag.Tag;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {

    List<Tag> findByDeletedFalse();

    Optional<Tag> findByIdAndDeletedFalse(final Long id);
}
