package vlad.lailo.markup.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "operations")
public class Operation implements GrantedAuthority {

    @Id
    private String id;

    @Override
    public String getAuthority() {
        return id;
    }
}
