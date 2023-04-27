package vlad.lailo.markup.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "datasets")
public class Dataset {

    @Id
    private String name;
    @JsonBackReference
    @ManyToMany(mappedBy = "datasets")
    private List<User> users = new LinkedList<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    //progress
    //who modify
    //status
    //owner
}
