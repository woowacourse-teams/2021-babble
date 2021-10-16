package gg.babble.babble.service;

import gg.babble.babble.domain.message.Content;
import gg.babble.babble.domain.user.User;
import gg.babble.babble.dto.request.MessageRequest;
import gg.babble.babble.dto.response.ChatResponse;
import gg.babble.babble.dto.response.EntryResponse;
import gg.babble.babble.dto.response.MessageResponse;
import gg.babble.babble.dto.response.UserResponse;
import javax.annotation.PostConstruct;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private static final String CHANNEL_NAME = "chat";

    private final UserService userService;
    private final RedisTemplate<String, Object> redisTemplate;

    public ChatService(final UserService userService, final RedisTemplate<String, Object> redisTemplate) {
        this.userService = userService;
        this.redisTemplate = redisTemplate;
    }

    public void sendChatMessage(final Long roomId, final MessageRequest messageRequest) {
        User user = userService.findById(messageRequest.getUserId());
        Content content = new Content(messageRequest.getContent());

        MessageResponse message = new MessageResponse(UserResponse.from(user), content.getValue(), messageRequest.getType());
        ChatResponse response = new ChatResponse(roomId, message);
        redisTemplate.convertAndSend(CHANNEL_NAME, response);
    }
}
