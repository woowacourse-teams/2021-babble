package gg.babble.babble.restdocs;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import gg.babble.babble.domain.Session;
import gg.babble.babble.domain.game.Game;
import gg.babble.babble.domain.room.MaxHeadCount;
import gg.babble.babble.domain.room.Room;
import gg.babble.babble.domain.tag.Tag;
import gg.babble.babble.domain.user.User;
import gg.babble.babble.dto.request.TagRequest;
import gg.babble.babble.dto.response.CreatedRoomResponse;
import gg.babble.babble.dto.response.FoundRoomResponse;
import gg.babble.babble.dto.response.GameResponse;
import gg.babble.babble.dto.response.TagResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.context.WebApplicationContext;

public class RoomApiDocumentTest extends ApiDocumentTest {

    private static final int COUNT_OF_ONE_PAGE = 16;
    private static final int ROOM_COUNT = 20;

    // 태그, 유저, 방, 게임
    private final List<Tag> tags = new ArrayList<>();
    private final List<Room> rooms = new ArrayList<>();
    private final List<Game> games = new ArrayList<>();

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        super.setUp(webApplicationContext, restDocumentation);
        tags.add(tagRepository.save(new Tag("실버")));
        tags.add(tagRepository.save(new Tag("2시간")));
        tags.add(tagRepository.save(new Tag("솔로랭크")));

        games.add(gameRepository.save(new Game("League Of Legends1", "image1")));
        games.add(gameRepository.save(new Game("League Of Legends2", "image2")));
        games.add(gameRepository.save(new Game("League Of Legends3", "image3")));

        for (int i = 0; i < ROOM_COUNT; i++) {
            User user = userRepository.save(new User("user" + i));
            Room room = roomRepository.save(new Room(games.get(0), tags, new MaxHeadCount(20)));
            rooms.add(room);
            sessionRepository.save(new Session(String.valueOf(i), user, room));
        }
    }

    @DisplayName("방을 생성한다.")
    @Test
    public void createRoomTest() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("gameId", games.get(0).getId());
        body.put("maxHeadCount", 20);
        body.put("tags", tagRequestsFromTags());
        final CreatedRoomResponse expected = new CreatedRoomResponse(
            null,
            null,
            new GameResponse(null, games.get(0).getName()),
            20,
            tagResponsesFromTags()
        );

        final MvcResult mvcResult = mockMvc.perform(post("/api/rooms").characterEncoding("utf-8")
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(body)).characterEncoding("utf-8"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isCreated())
            .andDo(document("create-room",
                requestFields(fieldWithPath("gameId").description("게임 Id"),
                    fieldWithPath("maxHeadCount").description("최대 참가 인원"),
                    fieldWithPath("tags[].id").description("태그 Id")),
                responseFields(fieldWithPath("roomId").description("방 Id"),
                    fieldWithPath("createdDate").description("방 생성 시각"),
                    fieldWithPath("game.id").description("게임 Id"),
                    fieldWithPath("game.name").description("게임 이름"),
                    fieldWithPath("maxHeadCount").description("최대 참가 인원"),
                    fieldWithPath("tags[].id").description("태그 Id"),
                    fieldWithPath("tags[].name").description("태그 이름")))
            ).andReturn();

        final CreatedRoomResponse response = getResponseAs(mvcResult, CreatedRoomResponse.class);

        assertThat(response).usingRecursiveComparison()
            .ignoringExpectedNullFields()
            .isEqualTo(expected);
    }

    private List<TagRequest> tagRequestsFromTags() {
        return tags.stream()
            .map(tag -> new TagRequest(tag.getId()))
            .collect(Collectors.toList());
    }

    private List<TagResponse> tagResponsesFromTags() {
        return tags.stream()
            .map(TagResponse::from)
            .collect(Collectors.toList());
    }

    @DisplayName("방을 조회한다.")
    @Test
    public void readRoomTest() throws Exception {
        final FoundRoomResponse expected = FoundRoomResponse.from(rooms.get(0));

        final MvcResult mvcResult = mockMvc.perform(get("/api/rooms/" + rooms.get(0).getId())
            .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andDo(document("read-room",
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
                    fieldWithPath("tags[].name").description("태그 이름")))
            ).andReturn();

        final FoundRoomResponse response = getResponseAs(mvcResult, FoundRoomResponse.class);
        assertThat(response).usingRecursiveComparison().isEqualTo(expected);
    }

    private void testRoomResponseOfOnePage(final ResultActions actions) throws Exception {
        for (int indexOfPage = 0; indexOfPage < COUNT_OF_ONE_PAGE; indexOfPage++) {
            actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[%d].roomId", indexOfPage).isNumber())
                .andExpect(jsonPath("$[%d].createdDate", indexOfPage).isString())
                .andExpect(jsonPath("$[%d].game.id", indexOfPage).value(games.get(0).getId()))
                .andExpect(jsonPath("$[%d].host.id", indexOfPage).isNumber())
                .andExpect(jsonPath("$[%d].game.name", indexOfPage).value(games.get(0).getName()))
                .andExpect(jsonPath("$[%d].host.nickname", indexOfPage).isString())
                .andExpect(jsonPath("$[%d].host.avatar", indexOfPage).isString())
                .andExpect(jsonPath("$[%d].headCount.current", indexOfPage).isNumber())
                .andExpect(jsonPath("$[%d].headCount.max", indexOfPage).isNumber())
                .andExpect(jsonPath("$[%d].tags", indexOfPage).isArray())
                .andExpect(jsonPath("$[%d].tags.length()", indexOfPage).value(tags.size()))
                .andExpect(jsonPath("$[%d].tags[0].name", indexOfPage).value(tags.get(0).getName()))
                .andExpect(jsonPath("$[%d].tags[1].name", indexOfPage).value(tags.get(1).getName()))
                .andExpect(jsonPath("$[%d].tags[2].name", indexOfPage).value(tags.get(2).getName()));
        }
    }

    private void describeRoomResponse(final ResultActions actions) throws Exception {
        actions.andDo(document("read-game-rooms-without-page-and-tags",
            responseFields(fieldWithPath("[].roomId").description("방 Id"),
                fieldWithPath("[].createdDate").description("방 생성 시각"),
                fieldWithPath("[].game.id").description("게임 Id"),
                fieldWithPath("[].game.name").description("게임 이름"),
                fieldWithPath("[].host.id").description("호스트 Id"),
                fieldWithPath("[].host.nickname").description("호스트 닉네임"),
                fieldWithPath("[].host.avatar").description("호스트 아바타"),
                fieldWithPath("[].headCount.current").description("현재 참가 인원"),
                fieldWithPath("[].headCount.max").description("최대 참가 인원"),
                fieldWithPath("[].tags[].id").description("태그 Id"),
                fieldWithPath("[].tags[].name").description("태그 이름"))));
    }

    @DisplayName("page 번호와 태그 없이 게임에 해당하는 방 목록을 조회한다.")
    @Test
    public void readGameRoomsWithoutPageAndTagsTest() throws Exception {
        final List<FoundRoomResponse> expected = rooms.stream()
            .sorted(Comparator.comparing(Room::getCreatedAt).reversed())
            .limit(COUNT_OF_ONE_PAGE)
            .filter(room -> room.getGame().getId().equals(games.get(0).getId()))
            .map(FoundRoomResponse::from)
            .collect(Collectors.toList());

        final ResultActions actions = mockMvc.perform(get("/api/rooms?gameId=" + games.get(0).getId())
            .accept(MediaType.APPLICATION_JSON_VALUE));
        describeRoomResponse(actions);
        final MvcResult mvcResult = actions.andReturn();
        final List<FoundRoomResponse> responses = Arrays.asList(getResponseAs(mvcResult, FoundRoomResponse[].class));

        assertThat(responses).usingRecursiveComparison().isEqualTo(expected);
    }

    @DisplayName("태그 없이 게임과 page 번호에 해당하는 방 목록을 조회한다.")
    @Test
    public void readGameRoomsWithoutTagsTest() throws Exception {
        final List<FoundRoomResponse> expected = rooms.stream()
            .sorted(Comparator.comparing(Room::getCreatedAt).reversed())
            .limit(COUNT_OF_ONE_PAGE)
            .filter(room -> room.getGame().getId().equals(games.get(0).getId()))
            .map(FoundRoomResponse::from)
            .collect(Collectors.toList());
        final ResultActions actions = mockMvc.perform(get("/api/rooms?gameId=" + games.get(0).getId() + "&page=1")
            .accept(MediaType.APPLICATION_JSON_VALUE));
        describeRoomResponse(actions);
        final MvcResult mvcResult = actions.andReturn();
        final List<FoundRoomResponse> responses = Arrays.asList(getResponseAs(mvcResult, FoundRoomResponse[].class));

        assertThat(responses).usingRecursiveComparison().isEqualTo(expected);
    }

    @DisplayName("page 번호 없이 게임과 태그에 해당하는 방 목록을 조회한다.")
    @Test
    public void readGameRoomsWithoutPageTest() throws Exception {
        final List<FoundRoomResponse> expected = rooms.stream()
            .sorted(Comparator.comparing(Room::getCreatedAt).reversed())
            .limit(COUNT_OF_ONE_PAGE)
            .filter(room -> room.getGame().getId().equals(games.get(0).getId()))
            .filter(room -> room.getTagRegistrationsOfRoom().tags().contains(tags.get(0))
                && room.getTagRegistrationsOfRoom().tags().contains(tags.get(1)))
            .map(FoundRoomResponse::from)
            .collect(Collectors.toList());
        final ResultActions actions = mockMvc
            .perform(get("/api/rooms?gameId=" + games.get(0).getId() + "&tagIds=" + tags.get(0).getId() + "," + tags.get(1).getId())
                .accept(MediaType.APPLICATION_JSON_VALUE));
        describeRoomResponse(actions);
        final MvcResult mvcResult = actions.andReturn();
        final List<FoundRoomResponse> responses = Arrays.asList(getResponseAs(mvcResult, FoundRoomResponse[].class));

        assertThat(responses).usingRecursiveComparison().isEqualTo(expected);
    }

    @DisplayName("게임과 page 번호, 태그에 해당하는 방 목록을 조회한다.")
    @Test
    public void readGameRoomsTest() throws Exception {
        final List<FoundRoomResponse> expected = rooms.stream()
            .sorted(Comparator.comparing(Room::getCreatedAt).reversed())
            .limit(COUNT_OF_ONE_PAGE)
            .filter(room -> room.getGame().getId().equals(games.get(0).getId()))
            .filter(room -> room.getTagRegistrationsOfRoom().tags().contains(tags.get(0))
                && room.getTagRegistrationsOfRoom().tags().contains(tags.get(1)))
            .map(FoundRoomResponse::from)
            .collect(Collectors.toList());
        final ResultActions actions = mockMvc
            .perform(get("/api/rooms?gameId=" + games.get(0).getId() + "&tagIds=" + tags.get(0).getId() + "," + tags.get(1).getId() + "&page=1")
                .accept(MediaType.APPLICATION_JSON_VALUE));
        describeRoomResponse(actions);
        final MvcResult mvcResult = actions.andReturn();
        final List<FoundRoomResponse> responses = Arrays.asList(getResponseAs(mvcResult, FoundRoomResponse[].class));

        assertThat(responses).usingRecursiveComparison().isEqualTo(expected);
    }
}

