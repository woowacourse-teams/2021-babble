package gg.babble.babble.controller;

import gg.babble.babble.dto.request.UserRequest;
import gg.babble.babble.dto.response.UserResponse;
import gg.babble.babble.service.UserService;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/api/users")
@RestController
public class UserController {

    private final UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponse> create(@Valid @RequestBody final UserRequest userRequest) {
        return ResponseEntity.ok(userService.save(userRequest));
    }
}
