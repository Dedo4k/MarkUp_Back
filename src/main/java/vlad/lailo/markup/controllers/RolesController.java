package vlad.lailo.markup.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vlad.lailo.markup.models.dto.OperationDto;
import vlad.lailo.markup.models.dto.RoleDto;
import vlad.lailo.markup.services.RolesService;

import java.util.List;

@RestController
@RequestMapping("/api/v2/roles")
public class RolesController {

    private final RolesService rolesService;

    public RolesController(RolesService rolesService) {
        this.rolesService = rolesService;
    }

    @GetMapping
    public ResponseEntity<List<RoleDto>> getAllRoles() {
        return ResponseEntity.ok(rolesService.getAllRoles().stream().map(RoleDto::fromModel).toList());
    }

    @GetMapping("/ops")
    public ResponseEntity<List<OperationDto>> getAllOperations() {
        return ResponseEntity.ok(rolesService.getAllOperations().stream().map(OperationDto::fromModel).toList());
    }
}
