package gg.babble.babble.service;

import gg.babble.babble.domain.User;
import gg.babble.babble.domain.UserRepository;
import gg.babble.babble.exception.BabbleNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new BabbleNotFoundException("존재하지 않는 유저 Id 입니다."));
    }
}
