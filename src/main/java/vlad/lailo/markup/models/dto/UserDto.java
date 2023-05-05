package vlad.lailo.markup.models.dto;

import vlad.lailo.markup.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserDto {

    public long id;

    public String username;

    public List<RoleDto> roles = new ArrayList<>();

    public boolean expired;

    public boolean locked;

    public boolean expiredCredentials;

    public boolean enabled;

    public List<DatasetDto> datasets = new ArrayList<>();

    //TODO make list of ids, call moderators info when it is needed
    public List<ModeratorDto> moderators = new ArrayList<>();

    public List<UserStatisticDto> userStatistics = new ArrayList<>();

    public static UserDto fromModel(User user) {
        UserDto dto = new UserDto();
        dto.id = user.getId();
        dto.username = user.getUsername();
        dto.roles = user.getRoles().stream().map(RoleDto::fromModel).collect(Collectors.toList());
        dto.enabled = user.isEnabled();
        dto.expired = !user.isAccountNonExpired();
        dto.expiredCredentials = !user.isCredentialsNonExpired();
        dto.locked = !user.isAccountNonLocked();
        dto.datasets.addAll(user.getDatasets().stream().map(DatasetDto::fromModel).toList());
        dto.moderators.addAll(user.getModerators().stream().map(ModeratorDto::fromModel).toList());
        dto.userStatistics.addAll(user.getUserStatistics().stream().map(UserStatisticDto::fromModel).toList());
        return dto;
    }
}
