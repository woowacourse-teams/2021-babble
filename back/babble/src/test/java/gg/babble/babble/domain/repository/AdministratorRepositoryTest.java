package gg.babble.babble.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;

import gg.babble.babble.domain.admin.Administrator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class AdministratorRepositoryTest {

    @Autowired
    private AdministratorRepository administratorRepository;

    @Test
    void existsAdministratorByIp() {
        Administrator junroot = administratorRepository.save(new Administrator("1.1.1.1", "junroot"));
        administratorRepository.deleteById(junroot.getId());

        assertThat(administratorRepository.existsAdministratorByIp(junroot.getIp())).isFalse();
    }
}
