package vlad.lailo.markup.models.dto;

import vlad.lailo.markup.models.Dataset;
import vlad.lailo.markup.models.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DatasetDto {

    public String name;

    public List<Long> userIds = new ArrayList<>();

    public LocalDateTime createdAt;

    public LocalDateTime updatedAt;

    public List<DatasetStatisticDto> datasetStatistics = new ArrayList<>();

    public static DatasetDto fromModel(Dataset dataset) {
        DatasetDto dto = new DatasetDto();
        dto.name = dataset.getName();
        dto.createdAt = dataset.getCreatedAt();
        dto.updatedAt = dataset.getUpdatedAt();
        dto.userIds.addAll(dataset.getUsers().stream().map(User::getId).toList());
        dto.datasetStatistics.addAll(dataset.getDatasetStatistics().stream()
                .map(DatasetStatisticDto::fromModel).toList());
        return dto;
    }
}
