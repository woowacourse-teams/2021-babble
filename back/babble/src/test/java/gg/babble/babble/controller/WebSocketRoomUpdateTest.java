package gg.babble.babble.controller;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;

import gg.babble.babble.domain.repository.RoomRepository;
import gg.babble.babble.domain.repository.UserRepository;
import gg.babble.babble.domain.room.Room;
import gg.babble.babble.domain.user.User;
import gg.babble.babble.dto.request.SessionRequest;
import gg.babble.babble.dto.response.SessionsResponse;
import gg.babble.babble.dto.response.UserResponse;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeoutException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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


public class WebSocketRoomUpdateTest extends ApplicationWebSocketTest {

    private static final String SUBSCRIBE_ROOM_UPDATE_BROAD_ENDPOINT_FORMAT = "/topic/rooms/%s/users";
    private static final String SEND_ROOM_UPDATE_ENDPOINT_FORMAT = "/ws/rooms/%s/users";

    private String subscribeRoomUpdateBroadEndpoint;
    private String sendRoomUpdateEndpoint;

    private User host;
    private User guest1;
    private User guest2;

    private String URL;
    private BlockingQueue<SessionsResponse> blockingQueue;
    private SessionsResponse expectedResponse;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    @BeforeEach
    protected void setup() {
        super.setUp();
        Room room = roomRepository.findAll().get(0);

        blockingQueue = new LinkedBlockingDeque<>();

        URL = "ws://localhost:" + port + "/connection";
        subscribeRoomUpdateBroadEndpoint = String.format(SUBSCRIBE_ROOM_UPDATE_BROAD_ENDPOINT_FORMAT, room.getId());
        sendRoomUpdateEndpoint = String.format(SEND_ROOM_UPDATE_ENDPOINT_FORMAT, room.getId());

        host = userRepository.findAll().get(0);
        guest1 = userRepository.findAll().get(1);
        guest2 = userRepository.findAll().get(2);

        expectedResponse = new SessionsResponse(
            UserResponse.from(host),
            Collections.singletonList(
                UserResponse.from(guest1)
            )
        );
    }

    @Sql(value = "classpath:websocket-context.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "classpath:websocket-clean.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("1번방에 입장하면, 자신을 포함한 1번방의 유저 정보들을 돌려받는다..")
    @Test
    public void testUserJoinUpdate() throws InterruptedException, ExecutionException, TimeoutException {
        // init setting
        WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(createTransportClient()));
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        userJoinAndBroadCasting(stompClient, new SessionRequest(host.getId(), "1111"));
        SessionsResponse response = blockingQueue.poll(5, SECONDS);
        assertThat(response).usingRecursiveComparison().isEqualTo(
            new SessionsResponse(
                UserResponse.from(host),
                Collections.emptyList()
            )
        );

//        justJoin(stompClient, new SessionRequest(host.getId(), "1111"));

        // Connection
        WebSocketStompClient stompClient2 = new WebSocketStompClient(new SockJsClient(createTransportClient()));
        stompClient2.setMessageConverter(new MappingJackson2MessageConverter());
        userJoinAndBroadCasting(stompClient2, new SessionRequest(guest1.getId(), "2222"));
        response = blockingQueue.poll(5, SECONDS);

        //then
        assertThat(response).usingRecursiveComparison().isEqualTo(expectedResponse);
    }

    private void justJoin(WebSocketStompClient stompClient, SessionRequest sessionRequest) throws ExecutionException, InterruptedException, TimeoutException {
        StompSession stompSession = stompClient.connect(URL, new StompSessionHandlerAdapter() {
        }).get(120, SECONDS);
        sendJoinMessage(stompSession, sessionRequest);
    }

    @Disabled
    @DisplayName("1번방에 있는 유저가 퇴장시, 자신을 포함한 1번방의 유저 정보들을 수신한다.")
    @Test
    public void testUserExitTest() throws InterruptedException, ExecutionException, TimeoutException {
        WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(createTransportClient()));
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        // host 입장.
        StompSession hostSession = stompClient.connect(URL, new StompSessionHandlerAdapter() {
        }).get(120, SECONDS);
        sendJoinMessage(hostSession, new SessionRequest(host.getId(), "1111"));

        // user1 입장
        StompSession user1Session = stompClient.connect(URL, new StompSessionHandlerAdapter() {
        }).get(120, SECONDS);
        sendJoinMessage(user1Session, new SessionRequest(guest1.getId(), "2222"));

        // user2 입장
        StompSession user2Session = stompClient.connect(URL, new StompSessionHandlerAdapter() {
        }).get(120, SECONDS);
        subscribeAndJoin(user2Session);

        // 연결끊기, user1 퇴장
        user1Session.disconnect();
        SessionsResponse response = blockingQueue.poll(5, SECONDS);  // 유저2는 현재방 현황을 수신

        expectedResponse = new SessionsResponse(
            UserResponse.from(host),
            Collections.singletonList(
                UserResponse.from(guest2)
            )
        );

        //then
        assertThat(response).usingRecursiveComparison().isEqualTo(expectedResponse);
    }

    private void subscribeAndJoin(final StompSession user2Session) {
        user2Session.subscribe(subscribeRoomUpdateBroadEndpoint, new UserUpdateStompFrameHandler());
        sendJoinMessage(user2Session, new SessionRequest(guest2.getId(), "3333"));
    }


    private void userJoinAndBroadCasting(WebSocketStompClient stompClient, SessionRequest request)
        throws InterruptedException, ExecutionException, TimeoutException {

        StompSession stompSession = stompClient.connect(URL, new StompSessionHandlerAdapter() {
        }).get(5, SECONDS);

        stompSession.subscribe(subscribeRoomUpdateBroadEndpoint, new UserUpdateStompFrameHandler());
        sendJoinMessage(stompSession, request);
    }

    private List<Transport> createTransportClient() {
        List<Transport> transports = new ArrayList<>();
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        return transports;
    }

    private void sendJoinMessage(StompSession stompSession, SessionRequest request) {
        stompSession.send(sendRoomUpdateEndpoint, request);
    }

    private class UserUpdateStompFrameHandler implements StompFrameHandler {

        @Override
        public Type getPayloadType(StompHeaders stompHeaders) {
            return SessionsResponse.class;
        }

        @Override
        public void handleFrame(StompHeaders stompHeaders, Object o) {
            System.out.println(o);
            blockingQueue.offer((SessionsResponse) o);
        }
    }
}