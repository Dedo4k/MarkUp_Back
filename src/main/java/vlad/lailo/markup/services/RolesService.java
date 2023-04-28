package vlad.lailo.markup.services;

import org.springframework.stereotype.Service;
import vlad.lailo.markup.exceptions.OperationNotFoundException;
import vlad.lailo.markup.exceptions.RoleNotFoundException;
import vlad.lailo.markup.models.Operation;
import vlad.lailo.markup.models.Role;
import vlad.lailo.markup.repository.OperationsRepository;
import vlad.lailo.markup.repository.RolesRepository;

import java.util.List;

@Service
public class RolesService {

    private final RolesRepository rolesRepository;
    private final OperationsRepository operationsRepository;

    public RolesService(RolesRepository rolesRepository,
                        OperationsRepository operationsRepository) {
        this.rolesRepository = rolesRepository;
        this.operationsRepository = operationsRepository;
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
}
