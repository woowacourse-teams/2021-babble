package gg.babble.babble;

import gg.babble.babble.domain.repository.AdministratorRepository;
import gg.babble.babble.domain.repository.AlternativeGameNameRepository;
import gg.babble.babble.domain.repository.AlternativeTagNameRepository;
import gg.babble.babble.domain.repository.GameRepository;
import gg.babble.babble.domain.repository.PostRepository;
import gg.babble.babble.domain.repository.RoomRepository;
import gg.babble.babble.domain.repository.S3Repository;
import gg.babble.babble.domain.repository.SessionRepository;
import gg.babble.babble.domain.repository.SliderRepository;
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
    protected S3Repository s3Repository;

    @Autowired
    protected AlternativeGameNameRepository alternativeGameNameRepository;

    @Autowired
    protected AlternativeTagNameRepository alternativeTagNameRepository;

    @Autowired
    protected TagRepository tagRepository;

    @Autowired
    protected SliderRepository sliderRepository;

    @Autowired
    protected PostRepository postRepository;

    @BeforeEach
    protected void setUp() {
        RestAssured.port = port;
    }
}
