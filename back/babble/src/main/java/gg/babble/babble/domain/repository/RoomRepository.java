package gg.babble.babble.domain.repository;

import gg.babble.babble.domain.room.Room;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoomRepository extends JpaRepository<Room, Long> {

    @Query("select tr.room "
        + "from TagRegistration tr "
        + "where tr.room.game.id = ?1 and tr.tag.id IN (?2) "
        + "group by tr.room.id "
        + "having count(tr.tag.id) = ?3 "
        + "order by tr.room.createdDate desc")
    List<Room> findAllByGameIdAndTagIds(final Long gameId, final List<Long> tagIds, final Long matchingCount, final Pageable pageable);

    @Query("select r "
        + "from Room r "
        + "where r.game.id = ?1 order by r.createdDate desc")
    List<Room> findAllByGameId(final Long gameId, final Pageable pageable);
}
