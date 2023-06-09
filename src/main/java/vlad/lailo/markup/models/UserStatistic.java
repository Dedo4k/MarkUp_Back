package vlad.lailo.markup.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "user_statistics")
public class UserStatistic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private User user;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime lastUpdateAt;

    private Duration totalTimeWorked;

    private long filesChecked;

    private long objectsChanged;
}
