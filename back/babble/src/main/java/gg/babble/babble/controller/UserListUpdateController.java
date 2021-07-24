package gg.babble.babble.controller;

import gg.babble.babble.dto.UserJoinRequest;
import gg.babble.babble.service.RoomService;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Controller
public class UserListUpdateController {

    private final SimpMessagingTemplate template;
    private final RoomService roomService;

    public UserListUpdateController(final SimpMessagingTemplate template, final RoomService roomService) {
        this.template = template;
        this.roomService = roomService;
    }

    @MessageMapping("/rooms/{roomId}/users")
    public void join(@DestinationVariable final Long roomId, final UserJoinRequest userJoinRequest) {
        template.convertAndSend(String.format("/topic/rooms/%s/users", roomId),
                roomService.sendJoinRoom(roomId, userJoinRequest));
    }

    @EventListener
    public void exit(final SessionDisconnectEvent event) {
        template.convertAndSend(
                String.format("/topic/rooms/%s/users", roomService.findRoomIdBySessionId(event.getSessionId())),
                roomService.sendExitRoom(event.getSessionId())
        );
    }
}
