package gg.babble.babble.controller;

import gg.babble.babble.dto.MessageRequest;
import gg.babble.babble.service.ChatService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {
    private final SimpMessagingTemplate template;

    public ChatController(final SimpMessagingTemplate template) {
        this.template = template;
    }

    @MessageMapping("/rooms/{roomId}/chat")
    public void chat(@DestinationVariable Long roomId, final MessageRequest messageRequest) {
        template.convertAndSend(String.format("/topic/rooms/%s/chat", roomId),
                ChatService.sendChatMessage(messageRequest));
    }
}
