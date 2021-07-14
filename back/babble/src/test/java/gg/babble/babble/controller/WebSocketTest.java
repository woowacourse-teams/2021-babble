package gg.babble.babble.controller;

import gg.babble.babble.AcceptanceTest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

public class WebSocketTest extends AcceptanceTest {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
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
}
