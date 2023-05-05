package vlad.lailo.markup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vlad.lailo.markup.models.DatasetStatistic;

import java.util.Optional;

public interface DatasetStatisticsRepository extends JpaRepository<DatasetStatistic, Long> {

    Optional<DatasetStatistic> findByDataset_NameAndUser_Id(String datasetName, long userId);
}
