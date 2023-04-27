package vlad.lailo.markup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vlad.lailo.markup.models.Dataset;

public interface DatasetRepository extends JpaRepository<Dataset, String> {

}
