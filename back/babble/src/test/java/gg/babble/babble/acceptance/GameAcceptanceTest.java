package gg.babble.babble.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import gg.babble.babble.domain.admin.Administrator;
import gg.babble.babble.domain.repository.AdministratorRepository;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class GameAcceptanceTest extends AuthTest {

    @Autowired
    private AdministratorRepository administratorRepository;

    @BeforeEach
    public void setUp() {
        super.setUp();
        RestAssured.port = port;
    }

    @DisplayName("등록되어 있는 IP의 경우 게임 생성을 할 수 있다.")
    @Test
    void postGameWithValidIp() {
        administratorRepository.save(new Administrator("127.0.0.1", "localhost"));
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
        // when
        ExtractableResponse<Response> response = 게임이_등록_됨("Maple Story", "abc");
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
