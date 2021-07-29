package gg.babble.babble.domain.repository;

import gg.babble.babble.domain.user.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByNickname(final String nickname);
}
