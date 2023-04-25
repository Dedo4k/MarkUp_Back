package vlad.lailo.markup.models.dto;

import vlad.lailo.markup.models.Role;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RoleDto {

    public String id;

    public List<OperationDto> operations = new ArrayList<>();

    public static RoleDto fromModel(Role role) {
        RoleDto dto = new RoleDto();
        dto.id = role.getAuthority();
        dto.operations = role.getAvailableOperations().stream()
                .map(OperationDto::fromModel)
                .collect(Collectors.toList());
        return dto;
    }
}
