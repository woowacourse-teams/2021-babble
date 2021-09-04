package gg.babble.babble;

import gg.babble.babble.domain.repository.AdministratorRepository;
import gg.babble.babble.domain.repository.GameRepository;
import gg.babble.babble.domain.repository.RoomRepository;
import gg.babble.babble.domain.repository.SessionRepository;
import gg.babble.babble.domain.repository.TagRepository;
import gg.babble.babble.domain.repository.UserRepository;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ApplicationTest {

    @LocalServerPort
    protected int port;

    @Autowired
    protected AdministratorRepository administratorRepository;

    @Autowired
    protected SessionRepository sessionRepository;

    @Autowired
    protected RoomRepository roomRepository;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected GameRepository gameRepository;

    @Autowired
    protected TagRepository tagRepository;

    @BeforeEach
    protected void setUp() {
        RestAssured.port = port;
    }
}
