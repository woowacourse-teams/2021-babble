package gg.babble.babble.controller;

import gg.babble.babble.dto.UserEnterRequest;
import gg.babble.babble.service.ChatService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class UserGroupController {
    private final SimpMessagingTemplate template;
    private final ChatService chatService;

    public UserGroupController(final SimpMessagingTemplate template, final ChatService chatService) {
        this.template = template;
        this.chatService = chatService;
    }

    @MessageMapping("/rooms/{roomId}/users")
    public void enter(@DestinationVariable Long roomId, final UserEnterRequest userEnterRequest) {
        template.convertAndSend(String.format("/topic/rooms/%s/users", roomId),
                chatService.sendEnterRoom(roomId, userEnterRequest.getUserId()));
    }
}
