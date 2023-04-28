package vlad.lailo.markup.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import vlad.lailo.markup.models.User;
import vlad.lailo.markup.models.dto.CreateUserDto;
import vlad.lailo.markup.models.dto.ModeratorDto;
import vlad.lailo.markup.models.dto.UpdateUserDto;
import vlad.lailo.markup.services.UserService;

@RestController
@RequestMapping("/api/v2/moderators")
public class ModeratorController {

    private final UserService userService;

    public ModeratorController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<ModeratorDto> createModerator(@RequestBody CreateUserDto createUserDto,
                                                        @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ModeratorDto.fromModel(userService.createModerator(createUserDto, user)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ModeratorDto> getModerator(@PathVariable long id) {
        return ResponseEntity.ok(ModeratorDto.fromModel(userService.getModerator(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ModeratorDto> updateModerator(@PathVariable long id, @RequestBody UpdateUserDto updateUserDto) {
        return ResponseEntity.ok(ModeratorDto.fromModel(userService.updateModerator(id, updateUserDto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ModeratorDto> deleteModerator(@PathVariable long id) {
        return ResponseEntity.ok(ModeratorDto.fromModel(userService.deleteModerator(id)));
    }
}
