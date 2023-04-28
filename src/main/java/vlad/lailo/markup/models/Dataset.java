package vlad.lailo.markup.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private List<User> users = new ArrayList<>();
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt;
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime updatedAt;

    //progress
    //who modify
    //status
    //owner
}
