package vlad.lailo.markup.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vlad.lailo.markup.models.Role;
import vlad.lailo.markup.models.User;
import vlad.lailo.markup.repository.UserRepository;

import java.util.List;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findUsersWithRole(Role role) {
        return userRepository.findAllByRolesContains(role);
    }
}
