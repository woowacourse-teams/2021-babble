package gg.babble.babble;

import gg.babble.babble.domain.admin.Administrator;
import gg.babble.babble.domain.repository.AdministratorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

@Profile("test")
@Transactional
@Configuration
public class AuthDataLoader implements CommandLineRunner {

    private final AdministratorRepository administratorRepository;

    public AuthDataLoader(final AdministratorRepository administratorRepository) {
        this.administratorRepository = administratorRepository;
    }

    @Override
    public void run(final String... args) throws Exception {
        administratorRepository.save(new Administrator("127.0.0.1", "localhost"));
    }
}
