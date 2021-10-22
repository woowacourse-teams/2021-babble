package gg.babble.babble.service.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import gg.babble.babble.dto.response.EntryResponse;
import gg.babble.babble.exception.BabbleIllegalArgumentException;
import java.io.IOException;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class EntrySubscriber implements RedisListener {

    private static final ChannelTopic CHANNEL_TOPIC = new ChannelTopic("users");

    private final ObjectMapper objectMapper;
    private final SimpMessagingTemplate messagingTemplate;

    public EntrySubscriber(final ObjectMapper objectMapper, final SimpMessagingTemplate messagingTemplate) {
        this.objectMapper = objectMapper;
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void onMessage(final Message message, final byte[] pattern) {
        try {
            EntryResponse response = objectMapper.readValue(message.getBody(), EntryResponse.class);
            messagingTemplate.convertAndSend(String.format("/topic/rooms/%s/users", response.getRoomId()), response.getSessionsResponse());
        } catch (IOException e) {
            throw new BabbleIllegalArgumentException("읽을 수 없는 메시지 형태 입니다.");
        }
    }

    @Override
    public ChannelTopic getChannelTopic() {
        return CHANNEL_TOPIC;
    }
}
