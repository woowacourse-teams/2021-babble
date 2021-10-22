package gg.babble.babble.domain.repository;

import gg.babble.babble.domain.tag.Tag;
import gg.babble.babble.dto.response.TagNameResponse;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TagRepository extends JpaRepository<Tag, Long> {

    List<Tag> findByDeletedFalse();

    Optional<Tag> findByIdAndDeletedFalse(final Long id);

    @Query(value = "select tag.id, tag.name, tag.deleted \n"
        + "from tag\n"
        + "    left join alternative_tag_name on alternative_tag_name.deleted = false and alternative_tag_name.tag_id = tag.id\n"
        + "where tag.name like %:keyword%\n"
        + "    or alternative_tag_name.name like %:keyword%", nativeQuery = true)
    List<Tag> findAllByKeyword(@Param("keyword")  final String keyword, final Pageable page);
}
