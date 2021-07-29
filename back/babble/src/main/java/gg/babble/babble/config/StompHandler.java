package gg.babble.babble.config;

import gg.babble.babble.service.SubscribeAuthService;
import java.util.Objects;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;

@Configuration
public class StompHandler implements ChannelInterceptor {

    private static final String DESTINATION_HEADER_KEY = "simpDestination";

    private final SubscribeAuthService subscribeAuthService;

    public StompHandler(final SubscribeAuthService subscribeAuthService) {
        this.subscribeAuthService = subscribeAuthService;
    }

    @Override
    public Message<?> preSend(final Message<?> message, final MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            subscribeValidate(message);
        }
        return message;
    }

    private void subscribeValidate(final Message<?> message) {
        String destinationUrl = (String) message.getHeaders().get(DESTINATION_HEADER_KEY);
        subscribeAuthService.validate(Objects.requireNonNull(destinationUrl));
    }
}