package vlad.lailo.markup.services;

import org.springframework.stereotype.Service;
import vlad.lailo.markup.exceptions.*;
import vlad.lailo.markup.models.Operation;
import vlad.lailo.markup.models.Role;
import vlad.lailo.markup.models.dto.CreateRoleDto;
import vlad.lailo.markup.models.dto.UpdateRoleDto;
import vlad.lailo.markup.repository.OperationsRepository;
import vlad.lailo.markup.repository.RolesRepository;

import java.util.List;
import java.util.Locale;

@Service
public class RolesService {

    private final RolesRepository rolesRepository;
    private final OperationsRepository operationsRepository;
    private final UserService userService;

    public RolesService(RolesRepository rolesRepository,
                        OperationsRepository operationsRepository,
                        UserService userService) {
        this.rolesRepository = rolesRepository;
        this.operationsRepository = operationsRepository;
        this.userService = userService;
    }

    public List<Role> getAllRoles() {
        return rolesRepository.findAll();
    }

    public Role getRole(String id) {
        return rolesRepository.findById(id).orElseThrow(() -> new RoleNotFoundException(id));
    }

    public List<Operation> getAllOperations() {
        return operationsRepository.findAll();
    }

    public Operation getOperation(String id) {
        return operationsRepository.findById(id).orElseThrow(() -> new OperationNotFoundException(id));
    }

    public Role createRole(CreateRoleDto createRoleDto) {
        Role role = new Role();
        if (!rolesRepository.existsById(createRoleDto.name)) {
            role.setId(createRoleDto.name.toUpperCase(Locale.ROOT));
            createRoleDto.operations.forEach(operationName -> {
                role.getOperations().add(operationsRepository.findById(operationName)
                        .orElseThrow(() -> new OperationNotFoundException(operationName)));
            });
            rolesRepository.save(role);
        } else {
            throw new RoleAlreadyExistsException(createRoleDto.name);
        }
        return role;
    }

    public Role updateRole(String id, UpdateRoleDto updateRoleDto) {
        Role role = rolesRepository.findById(id).orElseThrow(() -> new RoleNotFoundException(id));
        if (updateRoleDto.operations.isEmpty()) {
            throw new InvalidRoleOperationsException(id);
        }
        role.getOperations().clear();
        updateRoleDto.operations.forEach(operation -> role.getOperations().add(operationsRepository.findById(operation)
                .orElseThrow(() -> new OperationNotFoundException(operation))));
        rolesRepository.save(role);
        return role;
    }

    public Role deleteRole(String id) {
        Role role = rolesRepository.findById(id).orElseThrow(() -> new RoleNotFoundException(id));
        if (userService.findUsersWithRole(role).size() > 0) {
            throw new RoleDeleteException(id);
        }
        rolesRepository.delete(role);
        return role;
    }
}
