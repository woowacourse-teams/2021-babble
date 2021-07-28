package gg.babble.babble.controller;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import gg.babble.babble.AcceptanceTest;
import gg.babble.babble.dto.UserJoinRequest;
import gg.babble.babble.dto.UserListUpdateResponse;
import gg.babble.babble.dto.UserResponse;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

public class WebSocketRoomUpdateTest extends AcceptanceTest {

    @Value("${local.server.port}")
    private int port;
    private String URL;

    private static final String SUBSCRIBE_ROOM_UPDATE_BROAD_ENDPOINT = "/topic/rooms/1/users";
    private static final String SEND_ROOM_UPDATE_ENDPOINT = "/ws/rooms/1/users";

    private CompletableFuture<UserListUpdateResponse> completableFuture;

    @BeforeEach
    public void setup() {
        completableFuture = new CompletableFuture<>();
        URL = "ws://localhost:" + port + "/connection";
    }

    @DisplayName("1번방에 입장하면, 자신을 포함한 1번방의 유저 정보들을 돌려받는다..")
    @Test
    public void testUserUpdateEndpoint() throws InterruptedException, ExecutionException, TimeoutException, JsonProcessingException {
        UserListUpdateResponse expectedUserListUpdateResponse = new UserListUpdateResponse(
            new UserResponse(1L, "루트", "https://hyeon9mak.github.io/assets/images/9vatar.png"),
            Collections.singletonList(
                new UserResponse(2L, "와일더", "https://hyeon9mak.github.io/assets/images/9vatar.png")
            )
        );

        // init setting
        WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(createTransportClient()));
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        // Connection
        StompSession stompSession = stompClient.connect(URL, new StompSessionHandlerAdapter() {
        }).get(60, SECONDS);

        joinRoom(stompSession);
        sendJoinMessage(stompSession, new UserJoinRequest(2L, "7777"));
        UserListUpdateResponse userListUpdateResponse = completableFuture.get(5, SECONDS);

        //then
        assertThat(userListUpdateResponse).usingRecursiveComparison().isEqualTo(expectedUserListUpdateResponse);
    }

    private void joinRoom(StompSession stompSession) {
        stompSession.subscribe(SUBSCRIBE_ROOM_UPDATE_BROAD_ENDPOINT, new UserUpdateStompFrameHandler());
    }

    private List<Transport> createTransportClient() {
        List<Transport> transports = new ArrayList<>();
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        return transports;
    }

    private void sendJoinMessage(StompSession stompSession, UserJoinRequest userJoinRequest) throws JsonProcessingException {
        stompSession.send(SEND_ROOM_UPDATE_ENDPOINT, userJoinRequest);
    }

    private class UserUpdateStompFrameHandler implements StompFrameHandler {

        @Override
        public Type getPayloadType(StompHeaders stompHeaders) {
            return UserListUpdateResponse.class;
        }

        @Override
        public void handleFrame(StompHeaders stompHeaders, Object o) {
            System.out.println(o);
            completableFuture.complete((UserListUpdateResponse) o);
        }
    }
}
