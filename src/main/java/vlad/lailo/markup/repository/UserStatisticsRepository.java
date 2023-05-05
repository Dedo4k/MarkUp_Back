package vlad.lailo.markup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vlad.lailo.markup.models.UserStatistic;

import java.time.LocalDate;
import java.util.Optional;

public interface UserStatisticsRepository extends JpaRepository<UserStatistic, Long> {

    Optional<UserStatistic> findByDateAndUser_Id(LocalDate date, long userId);
}
