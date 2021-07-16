package gg.babble.babble.controller;

import gg.babble.babble.dto.UserJoinRequest;
import gg.babble.babble.service.ChatService;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Controller
public class UserJoinController {
    private final SimpMessagingTemplate template;
    private final ChatService chatService;

    public UserJoinController(final SimpMessagingTemplate template, final ChatService chatService) {
        this.template = template;
        this.chatService = chatService;
    }

    @MessageMapping("/rooms/{roomId}/users")
    public void join(@DestinationVariable final Long roomId, final UserJoinRequest userJoinRequest) {
        template.convertAndSend(String.format("/topic/rooms/%s/users", roomId),
                chatService.sendJoinRoom(roomId, userJoinRequest.getUserId()));
    }

    @EventListener
    public void onDisconnectEvent(final SessionDisconnectEvent event) {
        String sessionId = event.getSessionId();
        // xxService sessionId를 통해 userId를 얻고 여러 작업을 하게 수정
    }
}
