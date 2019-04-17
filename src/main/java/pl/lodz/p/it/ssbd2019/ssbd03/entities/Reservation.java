package pl.lodz.p.it.ssbd2019.ssbd03.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "reservations", schema = "public", catalog = "ssbd03")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    @EqualsAndHashCode.Exclude
    private long id;

    @ManyToOne(cascade=CascadeType.REFRESH)
    @JoinColumn(name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk__reservations__users", value = ConstraintMode.CONSTRAINT))
    private User user;

    @Column(name = "start_date", nullable = false)
    private Timestamp startDate;

    @Column(name = "end_date", nullable = false)
    private Timestamp endDate;

    @Min(1)
    @Column(name = "players_count", nullable = false)
    private int playersCount;

    @Column(name = "active", nullable = false)
    private boolean active;

    @ManyToOne
    @JoinColumn(name = "alley_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk__reservations__alleys", value = ConstraintMode.CONSTRAINT))
    private Alley alley;

    @Min(0)
    @Column(name = "version", nullable = false)
    private int version;
}
