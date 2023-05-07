package vlad.lailo.markup.models.dto;

import vlad.lailo.markup.models.UserStatistic;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class UserStatisticDto {

    public long userId;
    public LocalDate date;
    public LocalDateTime lastUpdateAt;
    public long totalTimeWorked;
    public long filesChecked;
    public long objectsChanged;

    public static UserStatisticDto fromModel(UserStatistic userStatistic) {
        UserStatisticDto dto = new UserStatisticDto();
        dto.userId = userStatistic.getUser().getId();
        dto.date = userStatistic.getDate();
        dto.lastUpdateAt = userStatistic.getLastUpdateAt();
        dto.totalTimeWorked = userStatistic.getTotalTimeWorked().toMillis();
        dto.filesChecked = userStatistic.getFilesChecked();
        dto.objectsChanged = userStatistic.getObjectsChanged();
        return dto;
    }
}
