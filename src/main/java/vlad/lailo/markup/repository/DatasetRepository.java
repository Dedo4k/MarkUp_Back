package vlad.lailo.markup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vlad.lailo.markup.models.Dataset;

@Repository
public interface DatasetRepository extends JpaRepository<Dataset, String> {

}
