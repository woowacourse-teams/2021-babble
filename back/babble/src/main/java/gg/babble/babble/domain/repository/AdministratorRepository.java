package gg.babble.babble.domain.repository;

import gg.babble.babble.domain.admin.Administrator;
import gg.babble.babble.domain.admin.Ip;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdministratorRepository extends JpaRepository<Administrator, Long> {

    boolean existsAdministratorByIp(final Ip ip);
}
