package vlad.lailo.markup.services;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vlad.lailo.markup.exceptions.ModeratorOwnerNotFoundException;
import vlad.lailo.markup.exceptions.UserNotFoundException;
import vlad.lailo.markup.models.User;
import vlad.lailo.markup.models.dto.CreateUserDto;
import vlad.lailo.markup.models.dto.UpdateUserDto;
import vlad.lailo.markup.repository.UserRepository;

@Service
@Transactional
public class ModeratorService {

    private final DatasetService datasetService;
    private final RolesService rolesService;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public ModeratorService(@Qualifier("localDatasetService") DatasetService datasetService,
                            UserRepository userRepository,
                            RolesService rolesService,
                            BCryptPasswordEncoder passwordEncoder) {
        this.datasetService = datasetService;
        this.rolesService = rolesService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User getModerator(long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    public User createModerator(CreateUserDto createUserDto, User owner) {
        User user = new User();
        user.setUsername(createUserDto.username);
        user.setPassword(passwordEncoder.encode(createUserDto.password));
        createUserDto.roles.forEach(role -> user.addRole(rolesService.getRole(role)));
        userRepository.save(user);
        datasetService.loadDatasets(createUserDto.datasets, user);
        owner.addModerator(user);
        userRepository.save(owner);
        return user;
    }

    public User updateModerator(long id, UpdateUserDto updateUserDto) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        if (updateUserDto.password != null && !updateUserDto.password.isBlank()) {
            user.setPassword(passwordEncoder.encode(updateUserDto.password));
        }
        user.getRoles().clear();
        updateUserDto.roles.forEach(role -> user.addRole(rolesService.getRole(role)));
        user.getDatasets().forEach(dataset -> dataset.getUsers().remove(user));
        user.getDatasets().clear();
        datasetService.loadDatasets(updateUserDto.datasets, user);
        userRepository.save(user);
        return user;
    }

    public User deleteModerator(long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        User owner = userRepository.findUserByModeratorsContains(user)
                .orElseThrow(() -> new ModeratorOwnerNotFoundException(id));
        owner.getModerators().remove(user);
        userRepository.save(owner);
        user.getDatasets().forEach(dataset -> {
            dataset.getUsers().remove(user);
            dataset.getDatasetStatistics().removeAll(user.getDatasetStatistics());
        });
        user.getDatasetStatistics().clear();
        user.getUserStatistics().clear();
        userRepository.delete(user);
        return user;
    }
}
