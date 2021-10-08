package gg.babble.babble.service.auth;

import gg.babble.babble.domain.admin.Administrator;
import gg.babble.babble.domain.admin.Ip;
import gg.babble.babble.domain.repository.AdministratorRepository;
import gg.babble.babble.dto.request.AdministratorRequest;
import gg.babble.babble.dto.response.AdministratorResponse;
import gg.babble.babble.exception.BabbleAuthenticationException;
import gg.babble.babble.exception.BabbleNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class AdministratorService {

    private final AdministratorRepository administratorRepository;

    public AdministratorService(final AdministratorRepository administratorRepository) {
        this.administratorRepository = administratorRepository;
    }

    public void validateIp(final String ip) {
        if (!administratorRepository.existsAdministratorByIp(new Ip(ip))) {
            throw new BabbleAuthenticationException(String.format("허용되지 않는 Ip입니다. (현재 Ip: %s)", ip));
        }
    }

    @Transactional
    public void deleteAdministrator(final Long id) {
        Administrator administrator = findById(id);
        administratorRepository.deleteById(administrator.getId());
    }

    private Administrator findById(final Long id) {
        return administratorRepository.findById(id)
            .orElseThrow(() -> new BabbleNotFoundException(String.format("존재하지 않는 관리자 Id(%d) 입니다.", id)));
    }

    public List<AdministratorResponse> findAll() {
        return administratorRepository.findAll()
            .stream()
            .map(AdministratorResponse::from)
            .collect(Collectors.toList());
    }

    public AdministratorResponse insert(final AdministratorRequest request) {
        return AdministratorResponse.from(administratorRepository.save(new Administrator(request.getIp(), request.getName())));
    }
}
