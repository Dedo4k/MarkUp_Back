package vlad.lailo.markup.models.dto;

import vlad.lailo.markup.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ModeratorDto {

    public long id;

    public String username;

    public List<RoleDto> roles = new ArrayList<>();

    public List<DatasetDto> datasets = new ArrayList<>();

    public static ModeratorDto fromModel(User user) {
        ModeratorDto dto = new ModeratorDto();
        dto.id = user.getId();
        dto.username = user.getUsername();
        dto.roles = user.getRoles().stream()
                .map(RoleDto::fromModel)
                .collect(Collectors.toList());
        dto.datasets = user.getDatasets().stream().map(DatasetDto::fromModel).toList();
        return dto;
    }
}
