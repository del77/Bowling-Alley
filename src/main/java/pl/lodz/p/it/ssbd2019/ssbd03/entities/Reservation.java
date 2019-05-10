package pl.lodz.p.it.ssbd2019.ssbd03.entities;

import lombok.*;
import pl.lodz.p.it.ssbd2019.ssbd03.entityvalidators.ValidReservationDates;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Min;
import java.sql.Timestamp;

/**
 * Klasa reprezentująca rezerwacje.
 */
@Entity
@Table(name = "reservations", schema = "public", catalog = "ssbd03")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ValidReservationDates
public class Reservation {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    @EqualsAndHashCode.Exclude
    private Long id;

    @ManyToOne(cascade=CascadeType.REFRESH)
    @JoinColumn(name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk__reservations__users", value = ConstraintMode.CONSTRAINT))
    private UserAccount userAccount;

    @Column(name = "start_date", nullable = false)
    @FutureOrPresent
    private Timestamp startDate;

    @Column(name = "end_date", nullable = false)
    @Future
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

    @Version
    @Min(0)
    @Column(name = "version", nullable = false)
    private long version;
}
