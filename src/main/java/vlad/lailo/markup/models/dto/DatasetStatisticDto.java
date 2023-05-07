package vlad.lailo.markup.models.dto;

import vlad.lailo.markup.models.DatasetStatistic;

public class DatasetStatisticDto {

    public String datasetName;
    public long userId;
    public long moderatingTime;

    public static DatasetStatisticDto fromModel(DatasetStatistic datasetStatistic) {
        DatasetStatisticDto dto = new DatasetStatisticDto();
        dto.datasetName = datasetStatistic.getDataset().getName();
        dto.userId = datasetStatistic.getUser().getId();
        dto.moderatingTime = datasetStatistic.getModeratingTime().toMillis();
        return dto;
    }
}
