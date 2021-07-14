package gg.babble.babble.domain.repository;

import gg.babble.babble.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
