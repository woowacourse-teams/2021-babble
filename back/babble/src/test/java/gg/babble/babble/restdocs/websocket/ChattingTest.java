package gg.babble.babble.restdocs.websocket;

import static gg.babble.babble.restdocs.GameApiDocumentTest.게임이_저장_됨;
import static gg.babble.babble.restdocs.RoomApiDocumentTest.방이_생성_됨;
import static gg.babble.babble.restdocs.TagApiDocumentTest.태그가_저장_됨;
import static gg.babble.babble.restdocs.UserApiDocumentTest.유저가_생성_됨;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;

import gg.babble.babble.dto.request.MessageRequest;
import gg.babble.babble.dto.request.SessionRequest;
import gg.babble.babble.dto.response.CreatedRoomResponse;
import gg.babble.babble.dto.response.GameWithImageResponse;
import gg.babble.babble.dto.response.MessageResponse;
import gg.babble.babble.dto.response.SessionsResponse;
import gg.babble.babble.dto.response.TagResponse;
import gg.babble.babble.dto.response.UserResponse;
import gg.babble.babble.restdocs.AcceptanceTest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeoutException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;

public class ChattingTest extends AcceptanceTest {

    private static final String SUBSCRIBE_ROOM_UPDATE_BROAD_ENDPOINT_FORMAT = "/topic/rooms/%s/users";
    private static final String SEND_ROOM_UPDATE_ENDPOINT_FORMAT = "/ws/rooms/%s/users";
    private static final String SUBSCRIBE_CHAT_UPDATE_BROAD_ENDPOINT_FORMAT = "/topic/rooms/%s/chat";
    private static final String SEND_CHAT_UPDATE_ENDPOINT_FORMAT = "/ws/rooms/%s/chat";

    private UserResponse host;
    private UserResponse guest1;
    private UserResponse guest2;
    private UserResponse guest3;
    private CreatedRoomResponse room;

    private BlockingQueue<SessionsResponse> blockingQueueForUsers;
    private BlockingQueue<MessageResponse> blockingQueueForChatting;
    private RoomContext roomContext;

    @BeforeEach
    protected void setUp(final RestDocumentationContextProvider restDocumentation) throws Exception {
        super.setUp(restDocumentation);
        blockingQueueForUsers = new LinkedBlockingDeque<>();
        blockingQueueForChatting = new LinkedBlockingDeque<>();
        roomContext = new RoomContext(blockingQueueForUsers, blockingQueueForChatting, port);

        localhost_관리자가_추가_됨();
        GameWithImageResponse game = 게임이_저장_됨("game1", Arrays.asList("게임 이미지1", "게임 이미지2", "게임 이미지3"), Collections.emptyList());
        TagResponse tag = 태그가_저장_됨("tag1", Collections.emptyList());

        host = 유저가_생성_됨("와일더");
        guest1 = 유저가_생성_됨("루트");
        guest2 = 유저가_생성_됨("포츈");
        guest3 = 유저가_생성_됨("현구막");

        room = 방이_생성_됨(game.getId(), Collections.singletonList(tag.getId()), 4);
    }

    @DisplayName("웹 소켓 연결시도에 성공하면, 200 OK 상태코드를 받는다.")
    @Test
    void webSocketConnectTest() {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .when()
            .get("/connection")
            .then().log().all()
            .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("1번방에 입장하면, 자신을 포함한 1번방의 유저 정보들을 돌려받는다.")
    @Test
    void testUserJoinUpdate() throws InterruptedException, ExecutionException, TimeoutException {
        // init setting
        유저가_방에_입장_함(host.getId(), room.getRoomId(), roomContext);
        SessionsResponse response = blockingQueueForUsers.poll(5, SECONDS);
        assertThat(response).usingRecursiveComparison().isEqualTo(
            new SessionsResponse(
                host,
                Collections.emptyList()
            )
        );

        // Connection
        유저가_방에_입장_함(guest1.getId(), room.getRoomId(), roomContext);
        response = blockingQueueForUsers.poll(5, SECONDS);

        //then
        assertThat(response).usingRecursiveComparison().isEqualTo(
            new SessionsResponse(
                host,
                Collections.singletonList(
                    guest1
                )
            )
        );
        blockingQueueForUsers.clear();
    }

    @DisplayName("1번방에 있는 유저가 퇴장시, 자신을 포함한 1번방의 유저 정보들을 수신한다.")
    @Test
    void testUserExitTest() throws InterruptedException, ExecutionException, TimeoutException {

        // host 입장 (host)
        유저가_방에_입장_함(host.getId(), room.getRoomId(), roomContext);
        SessionsResponse response = blockingQueueForUsers.poll(5, SECONDS);
        assertThat(response).usingRecursiveComparison().isEqualTo(
            new SessionsResponse(
                host,
                Collections.emptyList()
            )
        );

        // guest1 입장 (host, guest1)
        StompSession user1Session = 유저가_방에_입장_함(guest1.getId(), room.getRoomId(), roomContext);
        response = blockingQueueForUsers.poll(5, SECONDS);
        assertThat(response).usingRecursiveComparison().isEqualTo(
            new SessionsResponse(
                host,
                Collections.singletonList(
                    guest1
                )
            )
        );
        blockingQueueForUsers.poll(5, SECONDS);

        // guest2 입장 (host, guest1, guest2)
        유저가_방에_입장_함(guest2.getId(), room.getRoomId(), roomContext);
        response = blockingQueueForUsers.poll(5, SECONDS);
        assertThat(response).usingRecursiveComparison().isEqualTo(
            new SessionsResponse(
                host,
                Arrays.asList(
                    guest1,
                    guest2
                )
            )
        );
        blockingQueueForUsers.poll(5, SECONDS);
        blockingQueueForUsers.poll(5, SECONDS);

        // 연결끊기, guest1 퇴장 (host, guest2)
        user1Session.disconnect();
        response = blockingQueueForUsers.poll(20, SECONDS);  // 유저2는 현재방 현황을 수신

        //then
        assertThat(response).usingRecursiveComparison().isEqualTo(
            new SessionsResponse(
                host,
                Collections.singletonList(
                    guest2
                )
            )
        );
        blockingQueueForUsers.poll(5, SECONDS);

        // guest3 입장 (host, guest2, guest3)
        유저가_방에_입장_함(guest3.getId(), room.getRoomId(), roomContext);
        response = blockingQueueForUsers.poll(20, SECONDS);

        // then
        assertThat(response).usingRecursiveComparison().isEqualTo(
            new SessionsResponse(
                host,
                Arrays.asList(
                    guest2,
                    guest3
                )
            )
        );
        blockingQueueForUsers.clear();
    }

    @DisplayName("1번방에 유저가 입장하고, 메시지를 보내면, 메시지가 1번방에 브로드 캐스팅된다.")
    @Test
    void testUserUpdateEndpoint() throws InterruptedException, ExecutionException, TimeoutException {

        MessageResponse expectedMessageResponse = new MessageResponse(host, "철권 붐은 온다.", "chat");

        // init setting
        WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(createTransportClient()));
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        // Connection
        StompSession stompSession = 유저가_방에_입장_함(host.getId(), room.getRoomId(), roomContext);

        유저가_메시지를_보냄(stompSession, host.getId(), room.getRoomId(), "철권 붐은 온다.");
        MessageResponse messageResponse = blockingQueueForChatting.poll(5, SECONDS);

        //then
        assertThat(messageResponse).usingRecursiveComparison().isEqualTo(expectedMessageResponse);
    }

    public static StompSession 유저가_방에_입장_함(final Long userId, final Long roomId, final int port)
        throws ExecutionException, InterruptedException, TimeoutException {
        return 유저가_방에_입장_함(userId, roomId, new RoomContext(port));
    }

    private static StompSession 유저가_방에_입장_함(final Long userId, final Long roomId, final RoomContext roomContext)
        throws ExecutionException, InterruptedException, TimeoutException {
        List<Transport> transports = createTransportClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(transports));
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        StompSession stompSession = stompClient.connect(String.format("ws://localhost:%d/connection", roomContext.getPort()),
                new StompSessionHandlerAdapter() {
                })
            .get(20, SECONDS);
        String sessionId = ((CustomWebSocketTransport) transports.get(0)).getSessionId();

        stompSession.subscribe(String.format(SUBSCRIBE_ROOM_UPDATE_BROAD_ENDPOINT_FORMAT, roomId),
            new UserUpdateStompFrameHandler(roomContext.getBlockingQueueForUser()));
        stompSession.send(String.format(SEND_ROOM_UPDATE_ENDPOINT_FORMAT, roomId), new SessionRequest(userId, sessionId));
        stompSession.subscribe(String.format(SUBSCRIBE_CHAT_UPDATE_BROAD_ENDPOINT_FORMAT, roomId),
            new ChatUpdateStompFrameHandler(roomContext.getBlockingQueueForMessage()));

        return stompSession;
    }

    private static List<Transport> createTransportClient() {
        List<Transport> transports = new ArrayList<>();
        transports.add(new CustomWebSocketTransport(new StandardWebSocketClient()));
        return transports;
    }

    private static void 유저가_메시지를_보냄(final StompSession stompSession, final Long userId, final Long roomId, final String content) {
        stompSession.send(String.format(SEND_CHAT_UPDATE_ENDPOINT_FORMAT, roomId), new MessageRequest(userId, content, "chat"));
    }

    private static class UserUpdateStompFrameHandler implements StompFrameHandler {

        private final BlockingQueue<SessionsResponse> blockingQueue;

        public UserUpdateStompFrameHandler(final BlockingQueue<SessionsResponse> blockingQueue) {
            this.blockingQueue = blockingQueue;
        }

        @Override
        public Type getPayloadType(StompHeaders stompHeaders) {
            return SessionsResponse.class;
        }

        @Override
        public void handleFrame(StompHeaders stompHeaders, Object o) {
            blockingQueue.offer((SessionsResponse) o);
        }
    }

    private static class ChatUpdateStompFrameHandler implements StompFrameHandler {

        private final BlockingQueue<MessageResponse> blockingQueue;

        public ChatUpdateStompFrameHandler(final BlockingQueue<MessageResponse> blockingQueue) {
            this.blockingQueue = blockingQueue;
        }

        @Override
        public Type getPayloadType(StompHeaders stompHeaders) {
            return MessageResponse.class;
        }

        @Override
        public void handleFrame(StompHeaders stompHeaders, Object o) {
            System.out.println(o);
            blockingQueue.offer((MessageResponse) o);
        }
    }

    private static class RoomContext {

        private final BlockingQueue<SessionsResponse> blockingQueueForUser;
        private final BlockingQueue<MessageResponse> blockingQueueForMessage;
        private final int port;

        public RoomContext(final int port) {
            this(new LinkedBlockingDeque<>(), new LinkedBlockingDeque<>(), port);
        }

        public RoomContext(final BlockingQueue<SessionsResponse> blockingQueueForUser,
                           final BlockingQueue<MessageResponse> blockingQueueForMessage, final int port) {
            this.blockingQueueForUser = blockingQueueForUser;
            this.blockingQueueForMessage = blockingQueueForMessage;
            this.port = port;
        }

        public BlockingQueue<SessionsResponse> getBlockingQueueForUser() {
            return blockingQueueForUser;
        }

        public BlockingQueue<MessageResponse> getBlockingQueueForMessage() {
            return blockingQueueForMessage;
        }

        public int getPort() {
            return port;
        }
    }
}
