package vlad.lailo.markup.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import vlad.lailo.markup.models.User;
import vlad.lailo.markup.models.dto.CreateUserDto;
import vlad.lailo.markup.models.dto.ModeratorDto;
import vlad.lailo.markup.models.dto.UpdateUserDto;
import vlad.lailo.markup.services.ModeratorService;

@RestController
@RequestMapping("/api/v2/moderators")
public class ModeratorController {

    private final ModeratorService moderatorService;

    public ModeratorController(ModeratorService moderatorService) {
        this.moderatorService = moderatorService;
    }

    @PostMapping("/create")
    public ResponseEntity<ModeratorDto> createModerator(@RequestBody CreateUserDto createUserDto,
                                                        @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ModeratorDto.fromModel(moderatorService.createModerator(createUserDto, user)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ModeratorDto> getModerator(@PathVariable long id) {
        return ResponseEntity.ok(ModeratorDto.fromModel(moderatorService.getModerator(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ModeratorDto> updateModerator(@PathVariable long id, @RequestBody UpdateUserDto updateUserDto) {
        return ResponseEntity.ok(ModeratorDto.fromModel(moderatorService.updateModerator(id, updateUserDto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ModeratorDto> deleteModerator(@PathVariable long id) {
        return ResponseEntity.ok(ModeratorDto.fromModel(moderatorService.deleteModerator(id)));
    }
}
