package gg.babble.babble.controller.auth;

import gg.babble.babble.dto.response.AdministratorResponse;
import gg.babble.babble.service.auth.AdministratorService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @DeleteMapping("/{id}")
    private ResponseEntity<Void> deleteAdministrator(@PathVariable final Long id) {
        administratorService.deleteAdministrator(id);
        return ResponseEntity.noContent().build();
    }
}
