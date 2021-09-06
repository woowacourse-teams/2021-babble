package gg.babble.babble.controller;

import gg.babble.babble.dto.request.SessionRequest;
import gg.babble.babble.dto.response.ExitResponse;
import gg.babble.babble.service.EnterExitService;
import javax.validation.Valid;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Controller
public class EnterExitController {

    private final SimpMessagingTemplate template;
    private final EnterExitService enterExitService;

    public EnterExitController(final SimpMessagingTemplate template, final EnterExitService enterExitService) {
        this.template = template;
        this.enterExitService = enterExitService;
    }

    @MessageMapping("/rooms/{roomId}/users")
    public void enter(@DestinationVariable final Long roomId, @Valid final SessionRequest sessionRequest) {
        template.convertAndSend(String.format("/topic/rooms/%s/users", roomId),
            enterExitService.enter(roomId, sessionRequest));
    }

    @EventListener
    public void exit(final SessionDisconnectEvent event) {

        ExitResponse response = enterExitService.exit(event.getSessionId());
        template.convertAndSend(String.format("/topic/rooms/%s/users", response.getRoomId()), response.getSessionsResponse());
    }
}
