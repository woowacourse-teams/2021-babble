package gg.babble.babble.controller;

import gg.babble.babble.dto.request.MessageRequest;
import gg.babble.babble.service.ChatService;
import javax.validation.Valid;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    private final ChatService chatService;

    public ChatController(final ChatService chatService) {
        this.chatService = chatService;
    }

    @MessageMapping("/rooms/{roomId}/chat")
    public void chat(@DestinationVariable final Long roomId, @Valid final MessageRequest messageRequest) {
        chatService.sendChatMessage(roomId, messageRequest);
    }
}
