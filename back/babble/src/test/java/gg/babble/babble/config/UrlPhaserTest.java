package gg.babble.babble.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import gg.babble.babble.util.UrlPhaser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UrlPhaserTest {

    @DisplayName("올바른 구독 관련 url 주소를 넣으면, roomId를 파싱해서 리턴한다.")
    @Test
    void getRoomIdTest() {
        String userSubscribeUrl = "/topic/rooms/3/users";
        String chatSubscribeUrl = "/topic/rooms/3/chat";

        assertThat(UrlPhaser.getRoomId(userSubscribeUrl)).isEqualTo(3L);
        assertThat(UrlPhaser.getRoomId(chatSubscribeUrl)).isEqualTo(3L);
    }

    @DisplayName("올바르지 않은 url 주소를 넣으면, 예외가 발생한다.")
    @Test
    void wrongUrlException() {
        String wrongUrl = "/topic/jasonManse";
        assertThatThrownBy(() -> {
            UrlPhaser.getRoomId(wrongUrl);
        }).isInstanceOf(IllegalStateException.class);
    }
}