package gg.babble.babble.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import gg.babble.babble.ApplicationTest;
import gg.babble.babble.dto.response.AdministratorResponse;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class GameAcceptanceTest extends AuthTest {

    @BeforeEach
    public void setUp() {
        super.setUp();
        RestAssured.port = port;
    }

    @DisplayName("등록되어 있는 IP의 경우 게임 생성을 할 수 있다.")
    @Test
    void postGameWithValidIp() {
        ExtractableResponse<Response> response = 게임이_등록_됨("Maple Story", "abc");
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private ExtractableResponse<Response> 게임이_등록_됨(final String name, final String thumbnail) {
        Map<String, String> body = new HashMap<>();
        body.put("name", name);
        body.put("thumbnail", thumbnail);

        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(body)
            .when().post("/api/games")
            .then().extract();
    }

    @DisplayName("등록되어 있지않는 IP의 경우 게임을 생성할 수 없다.")
    @Test
    void postGameWithInvalidIp() {
        // given
        List<AdministratorResponse> adminResponses = 관리자_IP_전체_조회();

        ExtractableResponse<Response> deletedResponse = 관리자_IP가_삭제_됨(adminResponses.get(0).getId());
        assertThat(deletedResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        // when
        ExtractableResponse<Response> response = 게임이_등록_됨("Maple Story", "abc");
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    private ExtractableResponse<Response> 관리자_IP가_삭제_됨(final Long id) {
        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().delete("/api/admins/" + id).then().extract();
    }

    private List<AdministratorResponse> 관리자_IP_전체_조회() {
        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/api/admins").then().extract()
            .jsonPath().getList(".", AdministratorResponse.class);
    }
}
