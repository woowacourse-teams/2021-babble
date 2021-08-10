package gg.babble.babble.service;

import gg.babble.babble.domain.repository.UserRepository;
import gg.babble.babble.domain.user.User;
import gg.babble.babble.dto.request.UserRequest;
import gg.babble.babble.dto.response.UserResponse;
import gg.babble.babble.exception.BabbleNotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findById(final Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new BabbleNotFoundException("존재하지 않는 유저 Id 입니다."));
    }

    @Transactional
    public UserResponse save(final UserRequest request) {
        User user = userRepository.save(new User(request.getNickname()));
        return UserResponse.from(user);
    }
}
