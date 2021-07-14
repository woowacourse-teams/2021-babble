package gg.babble.babble.service;

import gg.babble.babble.domain.User;
import gg.babble.babble.dto.MessageRequest;
import gg.babble.babble.dto.MessageResponse;
import gg.babble.babble.dto.UserResponse;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ChatService {
    private static Map<Long, User> DB = new HashMap<Long, User>(){
        {
            put(1L, new User(1L, "forutne"));
        }
    };

    public static MessageResponse sendChatMessage(final MessageRequest messageRequest) {
        User user = DB.get(messageRequest.getUserId());
        String content = messageRequest.getContent();

        return MessageResponse.of(UserResponse.from(user), content);
    }
}
