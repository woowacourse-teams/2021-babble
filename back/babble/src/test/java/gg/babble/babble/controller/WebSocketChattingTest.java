package gg.babble.babble.controller;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;

import gg.babble.babble.ApplicationTest;
import gg.babble.babble.dto.request.MessageRequest;
import gg.babble.babble.dto.request.SessionRequest;
import gg.babble.babble.dto.response.MessageResponse;
import gg.babble.babble.dto.response.SessionsResponse;
import gg.babble.babble.dto.response.UserResponse;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

public class WebSocketChattingTest extends ApplicationTest {

    private static final String SUBSCRIBE_ROOM_UPDATE_BROAD_ENDPOINT = "/topic/rooms/1/users";
    private static final String SEND_ROOM_UPDATE_ENDPOINT = "/ws/rooms/1/users";
    private static final String SUBSCRIBE_CHAT_UPDATE_BROAD_ENDPOINT = "/topic/rooms/1/chat";
    private static final String SEND_CHAT_UPDATE_ENDPOINT = "/ws/rooms/1/chat";

    private String URL;
    private CompletableFuture<SessionsResponse> completableFutureUsers;
    private CompletableFuture<MessageResponse> completableFutureChat;

    @BeforeEach
    public void setup() {
        super.setUp();
        completableFutureUsers = new CompletableFuture<>();
        completableFutureChat = new CompletableFuture<>();
        URL = "ws://localhost:" + port + "/connection";
    }

    @DisplayName("1번방에 유저가 입장하고, 메시지를 보내면, 메시지가 1번방에 브로드 캐스팅된다.")
    @Test
    public void testUserUpdateEndpoint() throws InterruptedException, ExecutionException, TimeoutException {
        MessageResponse expectedMessageResponse = new MessageResponse(
            new UserResponse(2L, "와일더", "https://bucket-babble-front.s3.ap-northeast-2.amazonaws.com/img/users/profiles/profile66.png"),
            "철권 붐은 온다.", "chat"
        );

        // init setting
        WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(createTransportClient()));
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        // Connection
        StompSession stompSession = stompClient.connect(URL, new StompSessionHandlerAdapter() {
        }).get(60, SECONDS);

        joinRoom(stompSession);

        stompSession.subscribe(SUBSCRIBE_CHAT_UPDATE_BROAD_ENDPOINT, new ChatUpdateStompFrameHandler());
        stompSession.send(SEND_CHAT_UPDATE_ENDPOINT, new MessageRequest(2L, "철권 붐은 온다.", "chat"));
        MessageResponse messageResponse = completableFutureChat.get(5, SECONDS);

        //then
        assertThat(messageResponse).usingRecursiveComparison().isEqualTo(expectedMessageResponse);
    }

    private void joinRoom(StompSession stompSession) {
        stompSession.subscribe(SUBSCRIBE_ROOM_UPDATE_BROAD_ENDPOINT, new UserUpdateStompFrameHandler());
        sendJoinMessage(stompSession, new SessionRequest(2L, "7777"));
    }

    private List<Transport> createTransportClient() {
        List<Transport> transports = new ArrayList<>();
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        return transports;
    }

    private void sendJoinMessage(StompSession stompSession, SessionRequest sessionRequest) {
        stompSession.send(SEND_ROOM_UPDATE_ENDPOINT, sessionRequest);
    }

    private class ChatUpdateStompFrameHandler implements StompFrameHandler {

        @Override
        public Type getPayloadType(StompHeaders stompHeaders) {
            return MessageResponse.class;
        }

        @Override
        public void handleFrame(StompHeaders stompHeaders, Object o) {
            System.out.println(o);
            completableFutureChat.complete((MessageResponse) o);
        }
    }

    private class UserUpdateStompFrameHandler implements StompFrameHandler {

        @Override
        public Type getPayloadType(StompHeaders stompHeaders) {
            return SessionsResponse.class;
        }

        @Override
        public void handleFrame(StompHeaders stompHeaders, Object o) {
            System.out.println(o);
            completableFutureUsers.complete((SessionsResponse) o);
        }
    }
}
