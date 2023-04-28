package vlad.lailo.markup.services;

import org.springframework.stereotype.Service;
import vlad.lailo.markup.exceptions.RoleNotFoundException;
import vlad.lailo.markup.models.Role;
import vlad.lailo.markup.repository.RolesRepository;

import java.util.List;

@Service
public class RolesService {

    private final RolesRepository rolesRepository;

    public RolesService(RolesRepository rolesRepository) {
        this.rolesRepository = rolesRepository;
    }

    public List<Role> getAllRoles() {
        return rolesRepository.findAll();
    }

    public Role getRole(String id) {
        return rolesRepository.findById(id).orElseThrow(() -> new RoleNotFoundException(id));
    }
}