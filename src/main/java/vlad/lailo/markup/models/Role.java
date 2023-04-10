package vlad.lailo.markup.models;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {

    @Id
    private String id;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private final List<Operation> operations = new ArrayList<>();

    @Override
    public String getAuthority() {
        return id;
    }

    public List<Operation> getAvailableOperations() {
        return operations;
    }
}
