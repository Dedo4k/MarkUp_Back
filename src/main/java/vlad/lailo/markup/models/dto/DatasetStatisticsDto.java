package vlad.lailo.markup.models.dto;

import vlad.lailo.markup.models.DatasetStatistic;

import java.time.Duration;

public class DatasetStatisticsDto {

    public String datasetName;
    public long userId;
    public Duration moderatingTime;

    public static DatasetStatisticsDto fromModel(DatasetStatistic datasetStatistic) {
        DatasetStatisticsDto dto = new DatasetStatisticsDto();
        dto.datasetName = datasetStatistic.getDataset().getName();
        dto.userId = datasetStatistic.getUser().getId();
        dto.moderatingTime = datasetStatistic.getModeratingTime();
        return dto;
    }
}
