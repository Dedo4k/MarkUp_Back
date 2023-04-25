package vlad.lailo.markup.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vlad.lailo.markup.models.User;
import vlad.lailo.markup.models.dto.UserDto;

@RestController
@RequestMapping("/api/v2/auth")
public class AuthController {

    @GetMapping
    public ResponseEntity<UserDto> authUser(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(UserDto.fromModel(user));
    }
}
