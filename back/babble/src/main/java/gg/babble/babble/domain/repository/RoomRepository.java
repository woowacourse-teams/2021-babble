package gg.babble.babble.domain.repository;

import gg.babble.babble.domain.room.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {

}
