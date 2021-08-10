package gg.babble.babble.service.auth;

import gg.babble.babble.domain.repository.AdministratorRepository;
import gg.babble.babble.exception.BabbleAuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class AdminAuthService {

    private final AdministratorRepository administratorRepository;

    public AdminAuthService(final AdministratorRepository administratorRepository) {
        this.administratorRepository = administratorRepository;
    }

    public void validateIp(final String ip) {
        if(!administratorRepository.existsAdministratorByIp(ip)) {
            throw new BabbleAuthenticationException(String.format("허용되지 않는 Ip입니다. (현재 Ip: %s)", ip));
        }
    }
}
