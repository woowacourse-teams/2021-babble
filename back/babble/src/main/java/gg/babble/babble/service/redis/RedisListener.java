package gg.babble.babble.service.redis;

import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;

public interface RedisListener extends MessageListener {
    ChannelTopic getChannelTopic();
}
