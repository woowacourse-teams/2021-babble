package gg.babble.babble.acceptance;

import gg.babble.babble.ApplicationTest;
import gg.babble.babble.domain.admin.Administrator;
import gg.babble.babble.domain.repository.AdministratorRepository;
import io.restassured.RestAssured;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class GameE2ETest extends ApplicationTest {

    @BeforeEach
    public void setUp() {
        super.setUp();
        RestAssured.port = port;
    }

    @DisplayName("등록되어 있는 IP의 경우 게임 생성을 할 수 있다.")
    @Test
    void postGameWithValidIp() {
        // when
        Map<String, String> body = new HashMap<>();
        body.put("name", "Maple Story");
        body.put("thumbnail", "abc");

        // then
        RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(body)
            .when().post("/api/games")
            .then().log().all()
            .statusCode(HttpStatus.CREATED.value());
    }

}
