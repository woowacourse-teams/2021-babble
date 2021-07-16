package gg.babble.babble.service;

import gg.babble.babble.domain.User;
import gg.babble.babble.dto.MessageRequest;
import gg.babble.babble.dto.MessageResponse;
import gg.babble.babble.dto.UserResponse;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final UserService userService;

    public ChatService(final UserService userService) {
        this.userService = userService;
    }

    public MessageResponse sendChatMessage(final MessageRequest messageRequest) {
        User user = userService.findById(messageRequest.getUserId());
        String content = messageRequest.getContent();
        return MessageResponse.of(UserResponse.from(user), content);
    }
}
