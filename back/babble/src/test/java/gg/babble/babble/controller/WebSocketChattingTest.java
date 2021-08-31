package gg.babble.babble.controller;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;

import gg.babble.babble.domain.repository.RoomRepository;
import gg.babble.babble.domain.repository.UserRepository;
import gg.babble.babble.domain.room.Room;
import gg.babble.babble.domain.user.User;
import gg.babble.babble.dto.request.MessageRequest;
import gg.babble.babble.dto.request.SessionRequest;
import gg.babble.babble.dto.response.MessageResponse;
import gg.babble.babble.dto.response.SessionsResponse;
import gg.babble.babble.dto.response.UserResponse;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeoutException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

public class WebSocketChattingTest extends ApplicationWebSocketTest {

    private static final String SUBSCRIBE_ROOM_UPDATE_BROAD_ENDPOINT_FORMAT = "/topic/rooms/%s/users";
    private static final String SEND_ROOM_UPDATE_ENDPOINT_FORMAT = "/ws/rooms/%s/users";
    private static final String SUBSCRIBE_CHAT_UPDATE_BROAD_ENDPOINT_FORMAT = "/topic/rooms/%s/chat";
    private static final String SEND_CHAT_UPDATE_ENDPOINT_FORMAT = "/ws/rooms/%s/chat";

    private String subscribeRoomUpdateBroadEndpoint;
    private String sendRoomUpdateEndpoint;
    private String subscribeChatUpdateBroadEndpoint;
    private String sendChatUpdateEndpoint;

    private String URL;
    private User user;
    private BlockingQueue<SessionsResponse> blockingQueueForUsers;
    private BlockingQueue<MessageResponse> blockingQueueForChatting;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    @BeforeEach
    public void setup() {
        super.setUp();
        blockingQueueForUsers = new LinkedBlockingDeque<>();
        blockingQueueForChatting = new LinkedBlockingDeque<>();
        URL = "ws://localhost:" + port + "/connection";
        user = userRepository.findAll().get(0);
        Room room = roomRepository.findAll().get(0);
        subscribeRoomUpdateBroadEndpoint = String.format(SUBSCRIBE_ROOM_UPDATE_BROAD_ENDPOINT_FORMAT, room.getId());
        sendRoomUpdateEndpoint = String.format(SEND_ROOM_UPDATE_ENDPOINT_FORMAT, room.getId());
        subscribeChatUpdateBroadEndpoint = String.format(SUBSCRIBE_CHAT_UPDATE_BROAD_ENDPOINT_FORMAT, room.getId());
        sendChatUpdateEndpoint = String.format(SEND_CHAT_UPDATE_ENDPOINT_FORMAT, room.getId());
    }

    @DisplayName("1번방에 유저가 입장하고, 메시지를 보내면, 메시지가 1번방에 브로드 캐스팅된다.")
    @Sql(value = "classpath:websocket-context.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "classpath:websocket-clean.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    public void testUserUpdateEndpoint() throws InterruptedException, ExecutionException, TimeoutException {

        MessageResponse expectedMessageResponse = new MessageResponse(UserResponse.from(user), "철권 붐은 온다.", "chat");

        // init setting
        WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(createTransportClient()));
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        // Connection
        StompSession stompSession = stompClient.connect(URL, new StompSessionHandlerAdapter() {
        }).get(60, SECONDS);

        joinRoom(stompSession);

        stompSession.subscribe(subscribeChatUpdateBroadEndpoint, new ChatUpdateStompFrameHandler());
        stompSession.send(sendChatUpdateEndpoint, new MessageRequest(user.getId(), "철권 붐은 온다.", "chat"));
        MessageResponse messageResponse = blockingQueueForChatting.poll(5, SECONDS);

        //then
        assertThat(messageResponse).usingRecursiveComparison().isEqualTo(expectedMessageResponse);
    }

    private void joinRoom(StompSession stompSession) {
        stompSession.subscribe(subscribeRoomUpdateBroadEndpoint, new UserUpdateStompFrameHandler());
        stompSession.send(sendRoomUpdateEndpoint, new SessionRequest(user.getId(), "7777"));
    }

    private List<Transport> createTransportClient() {
        List<Transport> transports = new ArrayList<>();
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        return transports;
    }

    private class ChatUpdateStompFrameHandler implements StompFrameHandler {

        @Override
        public Type getPayloadType(StompHeaders stompHeaders) {
            return MessageResponse.class;
        }

        @Override
        public void handleFrame(StompHeaders stompHeaders, Object o) {
            System.out.println(o);
            blockingQueueForChatting.offer((MessageResponse) o);
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
            blockingQueueForUsers.offer((SessionsResponse) o);
        }
    }
}
