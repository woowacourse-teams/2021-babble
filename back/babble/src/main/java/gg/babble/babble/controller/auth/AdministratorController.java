package gg.babble.babble.controller.auth;

import gg.babble.babble.dto.request.AdministratorRequest;
import gg.babble.babble.dto.response.AdministratorResponse;
import gg.babble.babble.service.auth.AdministratorService;
import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/admins")
@RestController
public class AdministratorController {

    private final AdministratorService administratorService;

    public AdministratorController(final AdministratorService administratorService) {
        this.administratorService = administratorService;
    }

    @GetMapping
    private ResponseEntity<List<AdministratorResponse>> findAll() {
        return ResponseEntity.ok(administratorService.findAll());
    }

    @PostMapping
    private ResponseEntity<AdministratorResponse> insert(@Valid @RequestBody AdministratorRequest request) {
        AdministratorResponse response = administratorService.insert(request);
        return ResponseEntity.created(URI.create("/api/admins/" + response.getId())).body(response);
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<Void> deleteAdministrator(@PathVariable final Long id) {
        administratorService.deleteAdministrator(id);
        return ResponseEntity.noContent().build();
    }
}
