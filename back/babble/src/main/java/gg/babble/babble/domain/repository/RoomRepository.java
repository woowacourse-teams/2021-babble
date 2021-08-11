package gg.babble.babble.domain.repository;

import gg.babble.babble.domain.room.Room;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoomRepository extends JpaRepository<Room, Long> {

    @Query("select tr.room "
        + "from TagRegistration tr "
        + "where tr.room.deleted = false and tr.room.sessions.sessions.size > 0 and tr.room.game.id = ?1 and tr.tag.id IN (?2) "
        + "group by tr.room.id "
        + "having count(tr.tag.id) = ?3 "
        + "order by tr.room.createdAt desc")
    List<Room> findByGameIdAndTagIdsAndDeletedFalse(final Long gameId, final Set<Long> tagIds, final Long matchingCount, final Pageable pageable);

    @Query("select r "
        + "from Room r "
        + "where r.deleted = false and r.sessions.sessions.size > 0 and r.game.id = ?1 order by r.createdAt desc")
    List<Room> findByGameIdAndDeletedFalse(final Long gameId, final Pageable pageable);

    Optional<Room> findByIdAndDeletedFalse(final Long id);
}
