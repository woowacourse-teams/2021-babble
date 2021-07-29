package gg.babble.babble.service;

import gg.babble.babble.domain.user.User;
import gg.babble.babble.dto.request.MessageRequest;
import gg.babble.babble.dto.response.MessageResponse;
import gg.babble.babble.dto.response.UserResponse;
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
        return new MessageResponse(UserResponse.from(user), content);
    }
}
