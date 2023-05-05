package vlad.lailo.markup.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;

@Entity
@Getter
@Setter
@Table(name = "dataset_statistics")
public class DatasetStatistic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private Dataset dataset;

    @ManyToOne
    private User user;

    private Duration moderatingTime;
}
