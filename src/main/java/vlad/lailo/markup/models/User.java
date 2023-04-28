package vlad.lailo.markup.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToMany(fetch = FetchType.EAGER)
    private final List<Role> roles = new ArrayList<>();

    private String username;

    private String password;

    private boolean expired = false;

    private boolean locked = false;

    private boolean expiredCredentials = false;

    private boolean enabled = true;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    private List<Dataset> datasets = new ArrayList<>();

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    private List<User> moderators = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    public Collection<? extends GrantedAuthority> getAvailableOperations() {
        return roles.stream().map(Role::getAvailableOperations).flatMap(Collection::stream).toList();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !expired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !expiredCredentials;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void addDataset(Dataset dataset) {
        this.datasets.add(dataset);
        dataset.getUsers().add(this);
    }

    public void addModerator(User user) {
        this.moderators.add(user);
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }

    public boolean hasRole(String roleName) {
        return this.roles.stream().anyMatch(role -> role.getAuthority().equals(roleName));
    }
}
