package vlad.lailo.markup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vlad.lailo.markup.models.Operation;

public interface OperationsRepository extends JpaRepository<Operation, String> {
}
