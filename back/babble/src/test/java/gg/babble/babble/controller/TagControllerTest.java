package gg.babble.babble.controller;

import gg.babble.babble.AcceptanceTest;
import gg.babble.babble.dto.TagResponse;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TagControllerTest extends AcceptanceTest {
    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @DisplayName("전체 태그를 가져오는데 성공하면, 200응답 코드와 전체 태그를 가져온다.")
    @Test
    void webSocketConnectTest() {
        //given
        List<TagResponse> expectedTagResponses = Arrays.asList(
                TagResponse.builder()
                        .name("2시간")
                        .build(),
                TagResponse.builder()
                        .name("솔로랭크")
                        .build(),
                TagResponse.builder()
                        .name("실버")
                        .build()
        );

        //when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when()
                .get("/api/tags")
                .then().log().all()
                .extract();

        List<TagResponse> tagResponses = response.jsonPath().getList(".", TagResponse.class);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(tagResponses).usingRecursiveComparison().isEqualTo(expectedTagResponses);
    }
}