package vlad.lailo.markup.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vlad.lailo.markup.models.dto.CreateRoleDto;
import vlad.lailo.markup.models.dto.OperationDto;
import vlad.lailo.markup.models.dto.RoleDto;
import vlad.lailo.markup.models.dto.UpdateRoleDto;
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

    @PostMapping("/create")
    public ResponseEntity<RoleDto> createRole(@RequestBody CreateRoleDto createRoleDto) {
        return ResponseEntity.ok(RoleDto.fromModel(rolesService.createRole(createRoleDto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoleDto> updateRole(@PathVariable String id, @RequestBody UpdateRoleDto updateRoleDto) {
        return ResponseEntity.ok(RoleDto.fromModel(rolesService.updateRole(id, updateRoleDto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RoleDto> deleteRole(@PathVariable String id) {
        return ResponseEntity.ok(RoleDto.fromModel(rolesService.deleteRole(id)));
    }
}
