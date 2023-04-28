package vlad.lailo.markup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vlad.lailo.markup.models.Role;

@Repository
public interface RolesRepository extends JpaRepository<Role, String> {
}
