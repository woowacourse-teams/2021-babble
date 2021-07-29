package gg.babble.babble.restdocs;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import gg.babble.babble.ApplicationTest;
import gg.babble.babble.domain.repository.GameRepository;
import gg.babble.babble.domain.repository.RoomRepository;
import gg.babble.babble.dto.TagRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
public class RoomApiDocumentTest extends ApplicationTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private GameRepository gameRepository;

    private Long dummyRoomId;
    private Long dummyGameId;
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .apply(documentationConfiguration(restDocumentation))
            .alwaysDo(document("{method-name}", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
            .build();

        dummyRoomId = roomRepository.findAll().get(0).getId();
        dummyGameId = gameRepository.findAll().get(0).getId();
    }

    @DisplayName("방을 생성한다.")
    @Test
    public void createRoomTest() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("gameId", 1L);
        body.put("maxHeadCount", 20);
        body.put("tags",
            Arrays.asList(new TagRequest(1L), new TagRequest(2L), new TagRequest(3L))
        );
        mockMvc.perform(post("/api/rooms")
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(body)).characterEncoding("utf-8"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.roomId").isNumber())
            .andExpect(jsonPath("$.createdDate").isString())
            .andExpect(jsonPath("$.game.id").value(1L))
            .andExpect(jsonPath("$.game.name").value("League Of Legends"))
            .andExpect(jsonPath("$.tags").isArray())
            .andExpect(jsonPath("$.tags", hasSize(3)))
            .andExpect(jsonPath("$.maxHeadCount").value(20))
            .andExpect(jsonPath("$.tags[0].id").value(1L))
            .andExpect(jsonPath("$.tags[0].name").value("실버"))
            .andExpect(jsonPath("$.tags[1].id").value(2L))
            .andExpect(jsonPath("$.tags[1].name").value("2시간"))
            .andExpect(jsonPath("$.tags[2].id").value(3L))
            .andExpect(jsonPath("$.tags[2].name").value("솔로랭크"))

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
                    fieldWithPath("tags[].name").description("태그 리스트"))));
    }

    @DisplayName("방을 조회한다.")
    @Test
    public void readRoomTest() throws Exception {
        mockMvc.perform(get("/api/rooms/" + dummyRoomId)
            .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.roomId").value(dummyRoomId))
            .andExpect(jsonPath("$.createdDate").isString())
            .andExpect(jsonPath("$.game.id").value(1L))
            .andExpect(jsonPath("$.game.name").value("League Of Legends"))
            .andExpect(jsonPath("$.host.id").isNumber())
            .andExpect(jsonPath("$.host.nickname").isString())
            .andExpect(jsonPath("$.host.avatar").isString())
            .andExpect(jsonPath("$.headCount.current").isNumber())
            .andExpect(jsonPath("$.headCount.max").isNumber())
            .andExpect(jsonPath("$.tags").isArray())
            .andExpect(jsonPath("$.tags", hasSize(2)))
            .andExpect(jsonPath("$.tags[0].id").value(1L))
            .andExpect(jsonPath("$.tags[0].name").value("실버"))
            .andExpect(jsonPath("$.tags[1].id").value(2L))
            .andExpect(jsonPath("$.tags[1].name").value("2시간"))

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
                    fieldWithPath("tags[].name").description("태그 이름"))));
    }

    @DisplayName("page 번호와 태그 없이 게임에 해당하는 방 목록을 조회한다.")
    @Test
    public void readGameRoomsWithoutPageAndTagsTest() throws Exception {
        // GET /api/rooms?gameId=2
        // - 한 `page에 16개`
        // - page는 1부터 시작한다.
        int countOfOnePage = 16;

        ResultActions actions = mockMvc.perform(get("/api/rooms?gameId=" + dummyGameId)
            .accept(MediaType.APPLICATION_JSON_VALUE));

        for (int i = 0; i < countOfOnePage; i++) {
            actions.andExpect(status().isOk())
                .andExpect(jsonPath("$[" + i + "].roomId").isNumber())
                .andExpect(jsonPath("$[" + i + "].createdDate").isString())
                .andExpect(jsonPath("$[" + i + "].game.id").value(dummyGameId))
                .andExpect(jsonPath("$[" + i + "].game.name").value("League Of Legends"))
                .andExpect(jsonPath("$[" + i + "].host.id").isNumber())
                .andExpect(jsonPath("$[" + i + "].host.nickname").isString())
                .andExpect(jsonPath("$[" + i + "].host.avatar").isString())
                .andExpect(jsonPath("$[" + i + "].headCount.current").isNumber())
                .andExpect(jsonPath("$[" + i + "].headCount.max").isNumber())
                .andExpect(jsonPath("$[" + i + "].tags").isArray())
                .andExpect(jsonPath("$[" + i + "].tags", hasSize(2)))
                .andExpect(jsonPath("$[" + i + "].tags[0].name").value("실버"))
                .andExpect(jsonPath("$[" + i + "].tags[1].name").value("2시간"));
        }

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

    @DisplayName("태그 없이 게임과 page 번호에 해당하는 방 목록을 조회한다.")
    @Test
    public void readGameRoomsWithoutTagsTest() throws Exception {
        // GET /api/rooms?gameId=2&page=1
        // - 한 `page에 16개`
        // - page는 1부터 시작한다.
        int countOfOnePage = 16;

        ResultActions actions = mockMvc.perform(get("/api/rooms?gameId=" + dummyGameId + "&page=1")
            .accept(MediaType.APPLICATION_JSON_VALUE));

        actions.andDo(MockMvcResultHandlers.print());

        for (int i = 0; i < countOfOnePage; i++) {
            actions.andExpect(status().isOk())
                .andExpect(jsonPath("$[" + i + "].roomId").isNumber())
                .andExpect(jsonPath("$[" + i + "].createdDate").isString())
                .andExpect(jsonPath("$[" + i + "].game.id").value(dummyGameId))
                .andExpect(jsonPath("$[" + i + "].game.name").value("League Of Legends"))
                .andExpect(jsonPath("$[" + i + "].host.id").isNumber())
                .andExpect(jsonPath("$[" + i + "].host.nickname").isString())
                .andExpect(jsonPath("$[" + i + "].host.avatar").isString())
                .andExpect(jsonPath("$[" + i + "].headCount.current").isNumber())
                .andExpect(jsonPath("$[" + i + "].headCount.max").isNumber())
                .andExpect(jsonPath("$[" + i + "].tags").isArray())
                .andExpect(jsonPath("$[" + i + "].tags", hasSize(2)))
                .andExpect(jsonPath("$[" + i + "].tags[0].name").value("실버"))
                .andExpect(jsonPath("$[" + i + "].tags[1].name").value("2시간"));
        }

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

    @DisplayName("page 번호 없이 게임과 태그에 해당하는 방 목록을 조회한다.")
    @Test
    public void readGameRoomsWithoutPageTest() throws Exception {
        // GET /api/rooms?gameId=2&tagIds=1,2,3
        // - 한 `page에 16개`
        // - page는 1부터 시작한다.
        int countOfOnePage = 16;

        ResultActions actions = mockMvc.perform(get("/api/rooms?gameId=" + dummyGameId + "&tagIds=1,2")
            .accept(MediaType.APPLICATION_JSON_VALUE));

        for (int i = 0; i < countOfOnePage; i++) {
            actions.andExpect(status().isOk())
                .andExpect(jsonPath("$[" + i + "].roomId").isNumber())
                .andExpect(jsonPath("$[" + i + "].createdDate").isString())
                .andExpect(jsonPath("$[" + i + "].game.id").value(dummyGameId))
                .andExpect(jsonPath("$[" + i + "].game.name").value("League Of Legends"))
                .andExpect(jsonPath("$[" + i + "].host.id").isNumber())
                .andExpect(jsonPath("$[" + i + "].host.nickname").isString())
                .andExpect(jsonPath("$[" + i + "].host.avatar").isString())
                .andExpect(jsonPath("$[" + i + "].headCount.current").isNumber())
                .andExpect(jsonPath("$[" + i + "].headCount.max").isNumber())
                .andExpect(jsonPath("$[" + i + "].tags").isArray())
                .andExpect(jsonPath("$[" + i + "].tags", hasSize(2)))
                .andExpect(jsonPath("$[" + i + "].tags[0].name").value("실버"))
                .andExpect(jsonPath("$[" + i + "].tags[1].name").value("2시간"));
        }

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

    @DisplayName("게임과 page 번호, 태그에 해당하는 방 목록을 조회한다.")
    @Test
    public void readGameRoomsTest() throws Exception {
        // GET /api/rooms?gameId=2&tagIds=1,2,3&page=1
        // - 한 `page에 16개`
        // - page는 1부터 시작한다.
        int countOfOnePage = 16;

        ResultActions actions = mockMvc.perform(get("/api/rooms?gameId=" + dummyGameId + "&tagIds=1,2&page=1")
            .accept(MediaType.APPLICATION_JSON_VALUE));

        for (int i = 0; i < countOfOnePage; i++) {
            actions.andExpect(status().isOk())
                .andExpect(jsonPath("$[" + i + "].roomId").isNumber())
                .andExpect(jsonPath("$[" + i + "].createdDate").isString())
                .andExpect(jsonPath("$[" + i + "].game.id").value(dummyGameId))
                .andExpect(jsonPath("$[" + i + "].game.name").value("League Of Legends"))
                .andExpect(jsonPath("$[" + i + "].host.id").isNumber())
                .andExpect(jsonPath("$[" + i + "].host.nickname").isString())
                .andExpect(jsonPath("$[" + i + "].host.avatar").isString())
                .andExpect(jsonPath("$[" + i + "].headCount.current").isNumber())
                .andExpect(jsonPath("$[" + i + "].headCount.max").isNumber())
                .andExpect(jsonPath("$[" + i + "].tags").isArray())
                .andExpect(jsonPath("$[" + i + "].tags", hasSize(2)))
                .andExpect(jsonPath("$[" + i + "].tags[0].name").value("실버"))
                .andExpect(jsonPath("$[" + i + "].tags[1].name").value("2시간"));
        }

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
}

