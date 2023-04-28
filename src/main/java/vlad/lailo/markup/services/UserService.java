package vlad.lailo.markup.services;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import vlad.lailo.markup.exceptions.RoleNotFoundException;
import vlad.lailo.markup.models.User;
import vlad.lailo.markup.models.dto.CreateUserDto;
import vlad.lailo.markup.repository.RolesRepository;
import vlad.lailo.markup.repository.UserRepository;

@Service
public class UserService {

    private final DatasetService datasetService;
    private final UserRepository userRepository;
    private final RolesRepository rolesRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(@Qualifier("localDatasetService") DatasetService datasetService,
                       UserRepository userRepository,
                       RolesRepository rolesRepository,
                       BCryptPasswordEncoder passwordEncoder) {
        this.datasetService = datasetService;
        this.userRepository = userRepository;
        this.rolesRepository = rolesRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createModerator(CreateUserDto createUserDto, User owner) {
        User user = new User();
        user.setUsername(createUserDto.username);
        user.setPassword(passwordEncoder.encode(createUserDto.password));
        datasetService.loadDatasets(createUserDto.datasets, user);
        createUserDto.roles.forEach(role -> user.addRole(rolesRepository.findById(role)
                .orElseThrow(() -> new RoleNotFoundException(role))));
        userRepository.save(user);
        owner.addModerator(user);
        userRepository.save(owner);
        return user;
    }
}
