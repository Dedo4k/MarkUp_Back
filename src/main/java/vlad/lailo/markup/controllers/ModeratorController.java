package vlad.lailo.markup.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vlad.lailo.markup.models.User;
import vlad.lailo.markup.models.dto.CreateUserDto;
import vlad.lailo.markup.models.dto.UserDto;
import vlad.lailo.markup.services.UserService;

@RestController
@RequestMapping("/api/v2/moderators")
public class ModeratorController {

    private final UserService userService;

    public ModeratorController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<UserDto> createModerator(@RequestBody CreateUserDto createUserDto,
                                                   @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(UserDto.fromModel(userService.createModerator(createUserDto, user)));
    }
}
