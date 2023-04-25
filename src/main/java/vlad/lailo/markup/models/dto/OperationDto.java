package vlad.lailo.markup.models.dto;

import vlad.lailo.markup.models.Operation;

public class OperationDto {

    public String id;

    public static OperationDto fromModel(Operation operation) {
        OperationDto dto = new OperationDto();
        dto.id = operation.getAuthority();
        return dto;
    }
}
