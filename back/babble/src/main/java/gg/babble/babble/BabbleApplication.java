package gg.babble.babble;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class BabbleApplication {

    // 테스트용 주석
    public static void main(final String[] args) {
        SpringApplication.run(BabbleApplication.class, args);
    }
}
