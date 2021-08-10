package gg.babble.babble.controller;

import gg.babble.babble.dto.request.SessionRequest;
import gg.babble.babble.service.SessionService;
import javax.validation.Valid;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Controller
public class SessionController {

    private final SimpMessagingTemplate template;
    private final SessionService sessionService;

    public SessionController(final SimpMessagingTemplate template, final SessionService sessionService) {
        this.template = template;
        this.sessionService = sessionService;
    }

    @MessageMapping("/rooms/{roomId}/users")
    public void createSession(@DestinationVariable final Long roomId, @Valid final SessionRequest sessionRequest) {
        template.convertAndSend(String.format("/topic/rooms/%s/users", roomId),
            sessionService.create(roomId, sessionRequest));
    }

    @EventListener
    public void deleteSession(final SessionDisconnectEvent event) {
        template.convertAndSend(
            String.format("/topic/rooms/%s/users", sessionService.findRoomIdBySessionId(event.getSessionId())),
            sessionService.delete(event.getSessionId())
        );
    }
}
