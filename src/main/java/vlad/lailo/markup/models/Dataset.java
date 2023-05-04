package vlad.lailo.markup.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "datasets")
public class Dataset {

    @Id
    private String name;

    @ManyToMany(mappedBy = "datasets", fetch = FetchType.EAGER)
    private List<User> users = new ArrayList<>();

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime updatedAt;

    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, mappedBy = "dataset",fetch = FetchType.EAGER)
    private List<DatasetStatistic> datasetStatistics = new ArrayList<>();
    //progress
    //owner
}
