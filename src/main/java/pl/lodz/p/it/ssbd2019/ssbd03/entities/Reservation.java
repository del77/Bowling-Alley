package pl.lodz.p.it.ssbd2019.ssbd03.entities;

import lombok.*;
import pl.lodz.p.it.ssbd2019.ssbd03.validators.ValidReservationDates;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
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
    @NotNull
    @JoinColumn(name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk__reservations__users", value = ConstraintMode.CONSTRAINT))
    private UserAccount userAccount;
    
    @NotNull
    @Column(name = "start_date", nullable = false)
    @FutureOrPresent
    private Timestamp startDate;
    
    @NotNull
    @Column(name = "end_date", nullable = false)
    @Future
    private Timestamp endDate;

    @Min(1)
    @NotNull
    @Column(name = "players_count", nullable = false)
    private int playersCount;
    
    @NotNull
    @Column(name = "active", nullable = false)
    private boolean active;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "alley_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk__reservations__alleys", value = ConstraintMode.CONSTRAINT))
    private Alley alley;

    @Version
    @NotNull
    @Min(0)
    @Column(name = "version", nullable = false)
    private long version;
}
