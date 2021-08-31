package gg.babble.babble.controller;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ApplicationWebSocketTest {

    @LocalServerPort
    protected int port;

    @BeforeEach
    protected void setUp() {
        RestAssured.port = port;
    }
}
