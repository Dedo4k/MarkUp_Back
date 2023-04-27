package vlad.lailo.markup.models.dto;

import vlad.lailo.markup.models.Dataset;
import vlad.lailo.markup.models.Role;
import vlad.lailo.markup.models.User;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class UserDto {

    public String username;

    public List<RoleDto> roles = new ArrayList<>();

    public boolean expired;

    public boolean locked;

    public boolean expiredCredentials;

    public boolean enabled;

    public List<Dataset> datasets = new LinkedList<>();

    public static UserDto fromModel(User user) {
        UserDto dto = new UserDto();
        dto.username = user.getUsername();
        dto.roles = user.getAuthorities().stream()
                .map(r -> (Role) r)
                .map(RoleDto::fromModel)
                .collect(Collectors.toList());
        dto.enabled = user.isEnabled();
        dto.expired = !user.isAccountNonExpired();
        dto.expiredCredentials = !user.isCredentialsNonExpired();
        dto.locked = !user.isAccountNonLocked();
        dto.datasets.addAll(user.getDatasets());
        return dto;
    }
}
