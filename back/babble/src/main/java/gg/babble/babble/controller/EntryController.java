package gg.babble.babble.controller;

import gg.babble.babble.dto.request.SessionRequest;
import gg.babble.babble.service.EntryService;
import javax.validation.Valid;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Controller
public class EntryController {

    private final EntryService entryService;

    public EntryController(final EntryService entryService) {
        this.entryService = entryService;
    }

    @MessageMapping("/rooms/{roomId}/users")
    public void enter(@DestinationVariable final Long roomId, @Valid final SessionRequest sessionRequest) {
        entryService.enter(roomId, sessionRequest);
    }

    @EventListener
    public void exit(final SessionDisconnectEvent event) {
        entryService.exit(event.getSessionId());
    }
}
