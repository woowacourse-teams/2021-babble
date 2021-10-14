package gg.babble.babble.restdocs;

import static gg.babble.babble.restdocs.GameApiDocumentTest.게임이_저장_됨;
import static gg.babble.babble.restdocs.TagApiDocumentTest.assertThatSameAlternativeTagNames;
import static gg.babble.babble.restdocs.TagApiDocumentTest.태그가_저장_됨;
import static gg.babble.babble.restdocs.UserApiDocumentTest.유저가_생성_됨;
import static gg.babble.babble.restdocs.websocket.ChattingTest.유저가_방에_입장_함;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import gg.babble.babble.dto.request.TagRequest;
import gg.babble.babble.dto.response.CreatedRoomResponse;
import gg.babble.babble.dto.response.FoundRoomResponse;
import gg.babble.babble.dto.response.GameWithImageResponse;
import gg.babble.babble.dto.response.TagResponse;
import gg.babble.babble.dto.response.UserResponse;
import gg.babble.babble.restdocs.client.ResponseRepository;
import gg.babble.babble.restdocs.preprocessor.BigListPreprocessor;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.RestDocumentationContextProvider;

public class RoomApiDocumentTest extends AcceptanceTest {

    private static final int COUNT_OF_ONE_PAGE = 16;
    private static final int ROOM_COUNT = 20;
    private static final String ALTERNATIVE_NAME = "실딱";
    private static final int MAX_HEAD_COUNT = 20;

    private ResponseRepository<TagResponse, Long> tagResponseRepository;
    private ResponseRepository<CreatedRoomResponse, Long> createdRoomResponseRepository;
    private ResponseRepository<UserResponse, Long> userResponseRepository;
    private Map<Long, Long> sessions;
    private GameWithImageResponse game;

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) throws Exception {
        super.setUp(restDocumentation);
        localhost_관리자가_추가_됨();
        tagResponseRepository = new ResponseRepository<>(TagResponse::getId);
        createdRoomResponseRepository = new ResponseRepository<>(CreatedRoomResponse::getRoomId);
        userResponseRepository = new ResponseRepository<>(UserResponse::getId);
        sessions = new HashMap<>();

        tagResponseRepository.add(태그가_저장_됨("실버", Collections.singletonList(ALTERNATIVE_NAME)));
        tagResponseRepository.add(태그가_저장_됨("2시간", Collections.emptyList()));
        tagResponseRepository.add(태그가_저장_됨("솔로랭크", Collections.emptyList()));

        game = 게임이_저장_됨("League Of Legends1", Collections.singletonList("image1"), Collections.emptyList());

        for (int i = 0; i < ROOM_COUNT; i++) {
            UserResponse user = 유저가_생성_됨("user" + i);
            CreatedRoomResponse room = 방이_생성_됨(game.getId(), tagResponseRepository.getAllIds(), MAX_HEAD_COUNT);

            userResponseRepository.add(user);
            createdRoomResponseRepository.add(room);
            유저가_방에_입장_함(user.getId(), room.getRoomId(), port);
            sessions.put(room.getRoomId(), user.getId());
        }
    }

    public static CreatedRoomResponse 방이_생성_됨(final Long gameId, final List<Long> tagIds, final int maxHeadCount) {
        Map<String, Object> body = new HashMap<>();
        body.put("gameId", gameId);
        body.put("tags", tagRequestsFromTagIds(tagIds));
        body.put("maxHeadCount", maxHeadCount);

        return given().body(body)
            .when().post("/api/rooms")
            .then().statusCode(HttpStatus.CREATED.value())
            .extract().as(CreatedRoomResponse.class);
    }

    private static List<TagRequest> tagRequestsFromTagIds(final List<Long> ids) {
        return ids.stream()
            .map(TagRequest::new)
            .collect(Collectors.toList());
    }

    @DisplayName("방을 생성한다.")
    @Test
    void createRoomTest() {
        Map<String, Object> body = new HashMap<>();
        body.put("gameId", game.getId());
        body.put("maxHeadCount", 20);
        body.put("tags", tagRequestsFromTagIds(tagResponseRepository.getAllIds()));

        CreatedRoomResponse response = given().body(body)
            .filter(document("create-room",
                requestFields(fieldWithPath("gameId").description("게임 Id"),
                    fieldWithPath("maxHeadCount").description("최대 참가 인원"),
                    fieldWithPath("tags[].id").description("태그 Id")),
                responseFields(fieldWithPath("roomId").description("방 Id"),
                    fieldWithPath("createdDate").description("방 생성 시각"),
                    fieldWithPath("game.id").description("게임 Id"),
                    fieldWithPath("game.name").description("게임 이름"),
                    fieldWithPath("maxHeadCount").description("최대 참가 인원"),
                    fieldWithPath("tags[].id").description("태그 Id"),
                    fieldWithPath("tags[].name").description("태그 이름"),
                    fieldWithPath("tags[].alternativeNames").description("대체 이름"),
                    fieldWithPath("tags[].alternativeNames[]").description("대체 이름 객체"),
                    fieldWithPath("tags[].alternativeNames[].id").description("대체 이름 ID"),
                    fieldWithPath("tags[].alternativeNames[].name").description("대체 이름"))))
            .when().post("/api/rooms")
            .then().statusCode(HttpStatus.CREATED.value())
            .extract().body().as(CreatedRoomResponse.class);

        assertThat(response.getRoomId()).isNotNull();
        assertThat(response.getCreatedDate()).isNotNull();
        assertThat(response.getGame().getId()).isEqualTo(game.getId());
        assertThat(response.getGame().getName()).isEqualTo(game.getName());
        assertThat(response.getTags()).hasSize(tagResponseRepository.getSize());

        assertThatSameTagsWithRepository(response.getTags());
    }

    private void assertThatSameTagsWithRepository(final List<TagResponse> tagResponses) {
        assertThat(tagResponses).hasSize(tagResponseRepository.getSize());

        for (TagResponse tagResponse : tagResponses) {
            TagResponse expectedTagResponse = tagResponseRepository.get(tagResponse.getId());
            assertThat(tagResponse.getId()).isEqualTo(expectedTagResponse.getId());
            assertThat(tagResponse.getName()).isEqualTo(expectedTagResponse.getName());
            assertThatSameAlternativeTagNames(tagResponse.getAlternativeNames(), expectedTagResponse.getAlternativeNames());
        }
    }

    @DisplayName("방을 조회한다.")
    @Test
    void readRoomTest() {
        Long idToFind = createdRoomResponseRepository.getAnyId();

        FoundRoomResponse response = given().filter(document("read-room",
                responseFields(fieldWithPath("roomId").description("방 Id"),
                    fieldWithPath("createdDate").description("방 생성 시각"),
                    fieldWithPath("game.id").description("게임 Id"),
                    fieldWithPath("game.name").description("게임 이름"),
                    fieldWithPath("host.id").description("호스트 Id"),
                    fieldWithPath("host.nickname").description("호스트 닉네임"),
                    fieldWithPath("host.avatar").description("호스트 아바타"),
                    fieldWithPath("headCount.current").description("현재 참가 인원"),
                    fieldWithPath("headCount.max").description("최대 참가 인원"),
                    fieldWithPath("tags[].id").description("태그 Id"),
                    fieldWithPath("tags[].name").description("태그 이름"),
                    fieldWithPath("tags[].alternativeNames").description("대체 이름 객체"),
                    fieldWithPath("tags[].alternativeNames[].id").description("대체 이름 Id"),
                    fieldWithPath("tags[].alternativeNames[].name").description("대체 이름 Name"))))
            .when().get("/api/rooms/{id}", idToFind)
            .then().statusCode(HttpStatus.OK.value())
            .extract().body().as(FoundRoomResponse.class);

        assertThatFoundRoomResponse(idToFind, response);
    }

    private void assertThatFoundRoomResponse(final Long roomId, final FoundRoomResponse response) {
        UserResponse expectedHost = userResponseRepository.get(sessions.get(roomId));

        assertThat(response.getRoomId()).isEqualTo(roomId);
        assertThat(response.getCreatedDate()).isNotNull();
        assertThat(response.getGame().getId()).isEqualTo(game.getId());
        assertThat(response.getGame().getName()).isEqualTo(game.getName());
        assertThat(response.getHost().getId()).isEqualTo(expectedHost.getId());
        assertThat(response.getHost().getNickname()).isEqualTo(expectedHost.getNickname());
        assertThat(response.getHost().getAvatar()).isEqualTo(expectedHost.getAvatar());
        assertThat(response.getHeadCount().getCurrent()).isEqualTo(1);
        assertThat(response.getHeadCount().getMax()).isEqualTo(MAX_HEAD_COUNT);
        assertThatSameTagsWithRepository(response.getTags());
    }

    @DisplayName("태그 없이 게임과 page 번호에 해당하는 방 목록을 조회한다.")
    @Test
    void readGameRoomsWithoutTagsTest() {
        List<FoundRoomResponse> responses = given()
            .when().get("/api/rooms?gameId={gameId}&page=1", game.getId())
            .then().statusCode(HttpStatus.OK.value())
            .extract().jsonPath().getList(".", FoundRoomResponse.class);

        assertThat(responses).hasSize(COUNT_OF_ONE_PAGE);
        for (FoundRoomResponse response : responses) {
            assertThatFoundRoomResponse(response.getRoomId(), response);
        }
    }

    @DisplayName("page 번호 없이 게임과 태그에 해당하는 방 목록을 조회한다.")
    @Test
    void readGameRoomsWithoutPageTest() {
        List<Long> tagIds = tagResponseRepository.getAllIds();
        List<FoundRoomResponse> responses = given()
            .when().get("/api/rooms?gameId={gameId}&tagIds={tagId1},{tagsId2}", game.getId(), tagIds.get(0), tagIds.get(1))
            .then().statusCode(HttpStatus.OK.value())
            .extract().jsonPath().getList(".", FoundRoomResponse.class);

        assertThat(responses).hasSize(COUNT_OF_ONE_PAGE);
        for (FoundRoomResponse response : responses) {
            assertThatFoundRoomResponse(response.getRoomId(), response);
        }
    }

    @DisplayName("게임과 page 번호, 태그에 해당하는 방 목록을 조회한다.")
    @Test
    void readGameRoomsTest() {
        List<Long> tagIds = tagResponseRepository.getAllIds();
        List<FoundRoomResponse> responses = given().filter(document("read-rooms",
                preprocessResponse(new BigListPreprocessor()),
                responseFields(
                    fieldWithPath("[].roomId").description("방 Id"),
                    fieldWithPath("[].createdDate").description("방 생성 시각"),
                    fieldWithPath("[].game.id").description("게임 Id"),
                    fieldWithPath("[].game.name").description("게임 이름"),
                    fieldWithPath("[].host.id").description("호스트 Id"),
                    fieldWithPath("[].host.nickname").description("호스트 닉네임"),
                    fieldWithPath("[].host.avatar").description("호스트 아바타"),
                    fieldWithPath("[].headCount.current").description("현재 참가 인원"),
                    fieldWithPath("[].headCount.max").description("최대 참가 인원"),
                    fieldWithPath("[].tags[].id").description("태그 Id"),
                    fieldWithPath("[].tags[].name").description("태그 이름"),
                    fieldWithPath("[].tags[].alternativeNames[]").description("대체 이름 객체"),
                    fieldWithPath("[].tags[].alternativeNames[].id").description("대체 이름 ID"),
                    fieldWithPath("[].tags[].alternativeNames[].name").description("대체 이름"))))
            .when().get("/api/rooms?gameId={gameId}&tagIds={tagId1},{tagId2}&page=1", game.getId(), tagIds.get(0), tagIds.get(1))
            .then().statusCode(HttpStatus.OK.value())
            .extract().body().jsonPath().getList(".", FoundRoomResponse.class);

        assertThat(responses).hasSize(COUNT_OF_ONE_PAGE);
        for (FoundRoomResponse response : responses) {
            assertThatFoundRoomResponse(response.getRoomId(), response);
        }
    }
}
