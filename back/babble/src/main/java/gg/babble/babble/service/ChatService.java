package gg.babble.babble.service;

import gg.babble.babble.domain.User;
import gg.babble.babble.dto.MessageRequest;
import gg.babble.babble.dto.MessageResponse;
import gg.babble.babble.dto.UserEnterResponse;
import gg.babble.babble.dto.UserResponse;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ChatService {
    private static Map<Long, User> USER_DB = new HashMap<Long, User>() {
        {
            put(1L, new User(1L, "forutne"));
            put(2L, new User(2L, "hyeon9mak"));
            put(3L, new User(3L, "air"));
        }
    };

    private static Map<Long, List<User>> ROOM_DB = new HashMap<>();

    public MessageResponse sendChatMessage(final MessageRequest messageRequest) {
        User user = USER_DB.get(messageRequest.getUserId());
        String content = messageRequest.getContent();

        return MessageResponse.of(UserResponse.from(user), content);
    }

    public UserEnterResponse sendEnterRoom(final Long roomId, final Long userId) {
        User user = USER_DB.get(userId);

        if (Objects.isNull(ROOM_DB.get(roomId))) {
            ROOM_DB.put(roomId, new ArrayList<>());
        }

        List<User> users = ROOM_DB.get(roomId);
        users.add(user);
        ROOM_DB.put(roomId, users);

        return UserEnterResponse.builder()
                .host(getHost(users))
                .guests(getGuests(users))
                .build();
    }

    private UserResponse getHost(final List<User> users) {
        return UserResponse.from(users.get(0));
    }

    private List<UserResponse> getGuests(final List<User> users) {
        return users.subList(1, users.size())
                .stream()
                .map(UserResponse::from)
                .collect(Collectors.toList());
    }
}
