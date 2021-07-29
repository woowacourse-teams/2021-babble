package gg.babble.babble.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import gg.babble.babble.exception.BabbleIllegalStatementException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UrlParserTest {

    @DisplayName("올바른 구독 관련 url 주소를 넣으면, roomId를 파싱해서 리턴한다.")
    @Test
    void getRoomIdTest() {
        String userSubscribeUrl = "/topic/rooms/3/users";
        String chatSubscribeUrl = "/topic/rooms/3/chat";

        assertThat(UrlParser.getRoomId(userSubscribeUrl)).isEqualTo(3L);
        assertThat(UrlParser.getRoomId(chatSubscribeUrl)).isEqualTo(3L);
    }

    @DisplayName("올바르지 않은 url 주소를 넣으면, 예외가 발생한다.")
    @Test
    void wrongUrlException() {
        String wrongUrl = "/topic/jasonManse";
        assertThatThrownBy(() -> {
            UrlParser.getRoomId(wrongUrl);
        }).isInstanceOf(BabbleIllegalStatementException.class).hasMessageContaining("roomId를 파싱할 수 없는 url 입니다.");
    }
}