package gg.babble.babble.controller;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;

import gg.babble.babble.ApplicationTest;
import gg.babble.babble.domain.repository.UserRepository;
import gg.babble.babble.domain.user.User;
import gg.babble.babble.dto.request.UserJoinRequest;
import gg.babble.babble.dto.response.UserListUpdateResponse;
import gg.babble.babble.dto.response.UserResponse;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
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
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

public class WebSocketRoomUpdateTest extends ApplicationTest {

    // TODO : 데이터로더에 비 의존적으로 리팩토링
//    private static final int FIRST_DATA_INDEX = 0;
//    private static final String LEAGUE_OF_LEGENDS = "League Of Legends";
//    private static final String 루트 = "루트";
//    private static final String 와일더 = "와일더";
//    private static final String 현구막 = "현구막";
//    private static final String 실버 = "실버";
//    private static final String _2시간 = "2시간";
//    private static final String 솔로랭크 = "솔로랭크";
    private static final String SUBSCRIBE_ROOM_UPDATE_BROAD_ENDPOINT = "/topic/rooms/1/users";
    private static final String SEND_ROOM_UPDATE_ENDPOINT = "/ws/rooms/1/users";

    // TODO : 데이터로더에 비 의존적으로 리팩토링
//    @Autowired
//    private GameRepository gameRepository;
//
    @Autowired
    private UserRepository userRepository;

    private User user2;
    private User user3;
    private User user4;
//
//    @Autowired
//    private TagRepository tagRepository;
//
//    @Autowired
//    private RoomRepository roomRepository;

    private String URL;
    private CompletableFuture<UserListUpdateResponse> completableFuture;
    private UserListUpdateResponse expectedUserListUpdateResponse;

    @BeforeEach
    protected void setup() {
        // TODO : 데이터로더에 비 의존적으로 리팩토링
//        dummyDataSet();

        super.setUp();
        completableFuture = new CompletableFuture<>();
        URL = "ws://localhost:" + port + "/connection";
        user2 = userRepository.findByNickname("user0").get(0);
        user3 = userRepository.findByNickname("와일더").get(0);
        user4 = userRepository.findByNickname("현구막").get(0);
        expectedUserListUpdateResponse = new UserListUpdateResponse(
            UserResponse.from(user2),
            Collections.singletonList(
                UserResponse.from(user3)
            )
        );
    }

    // TODO : 데이터로더에 비 의존적으로 리팩토링
//    private void dummyDataSet() {
//        gameRepository.save(new Game(LEAGUE_OF_LEGENDS, "https://static-cdn.jtvnw.net/ttv-boxart/League%20of%20Legends-1080x1436.jpg"));
//        userRepository.save(new User(루트));
//        userRepository.save(new User(와일더));
//        userRepository.save(new User(현구막));
//        tagRepository.save(new Tag(실버));
//        tagRepository.save(new Tag(_2시간));
//        tagRepository.save(new Tag(솔로랭크));
//
//        Game game = gameRepository.findByName(LEAGUE_OF_LEGENDS).get(FIRST_DATA_INDEX);
//        User user = userRepository.findByNickname(루트).get(FIRST_DATA_INDEX);
//        List<Tag> tags = Arrays
//            .asList(tagRepository.findById(실버).orElseThrow(BabbleNotFoundException::new),
//                tagRepository.findById(_2시간).orElseThrow(BabbleNotFoundException::new));
//
//        Room room = new Room(game, tags, new MaxHeadCount(4));
//
//        room.join(user);
//        roomRepository.save(room);
//    }

    @DisplayName("1번방에 입장하면, 자신을 포함한 1번방의 유저 정보들을 돌려받는다..")
    @Test
    public void testUserJoinUpdate() throws InterruptedException, ExecutionException, TimeoutException {
        // init setting
        WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(createTransportClient()));
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        // Connection
        userJoinAndBroadCasting(stompClient, new UserJoinRequest(user3.getId(), "7777"));
        UserListUpdateResponse userListUpdateResponse = completableFuture.get(5, SECONDS);

        //then
        assertThat(userListUpdateResponse).usingRecursiveComparison().isEqualTo(expectedUserListUpdateResponse);
    }

    @Disabled
    @DisplayName("1번방에 있는 유저가 퇴장시, 자신을 포함한 1번방의 유저 정보들을 수신한다.")
    @Test
    public void testUserExitTest() throws InterruptedException, ExecutionException, TimeoutException {
        WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(createTransportClient()));
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        // 두번째 유저 입장.
        userJoinAndBroadCasting(stompClient, new UserJoinRequest(user3.getId(), "7777"));

        // 세번째 유저 입장
        StompSession stompSession2 = stompClient.connect(URL, new StompSessionHandlerAdapter() {
        }).get(60, SECONDS);

        joinRoom(stompSession2);
        sendJoinMessage(stompSession2, new UserJoinRequest(user4.getId(), "8888"));

        stompSession2.disconnect();

        completableFuture.get(5, SECONDS);  // 와일더(id = 2L) 입장 메시지
        completableFuture.get(5, SECONDS);  // 현구막(id = 3L) 입장메시지
        UserListUpdateResponse userListUpdateResponse = completableFuture.get(5, SECONDS);  // 현구막 퇴장

        //then
        assertThat(userListUpdateResponse).usingRecursiveComparison().isEqualTo(expectedUserListUpdateResponse);
    }

    private void userJoinAndBroadCasting(WebSocketStompClient stompClient, UserJoinRequest userJoinRequest)
        throws InterruptedException, ExecutionException, TimeoutException {
        StompSession stompSession = stompClient.connect(URL, new StompSessionHandlerAdapter() {
        }).get(60, SECONDS);

        joinRoom(stompSession);
        sendJoinMessage(stompSession, userJoinRequest);
    }

    private void joinRoom(StompSession stompSession) {
        stompSession.subscribe(SUBSCRIBE_ROOM_UPDATE_BROAD_ENDPOINT, new UserUpdateStompFrameHandler());
    }

    private List<Transport> createTransportClient() {
        List<Transport> transports = new ArrayList<>();
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        return transports;
    }

    private void sendJoinMessage(StompSession stompSession, UserJoinRequest userJoinRequest) {
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
