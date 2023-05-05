package vlad.lailo.markup.models.dto;

import vlad.lailo.markup.models.DatasetStatistic;

import java.time.Duration;

public class DatasetStatisticDto {

    public String datasetName;
    public long userId;
    public Duration moderatingTime;

    public static DatasetStatisticDto fromModel(DatasetStatistic datasetStatistic) {
        DatasetStatisticDto dto = new DatasetStatisticDto();
        dto.datasetName = datasetStatistic.getDataset().getName();
        dto.userId = datasetStatistic.getUser().getId();
        dto.moderatingTime = datasetStatistic.getModeratingTime();
        return dto;
    }
}
