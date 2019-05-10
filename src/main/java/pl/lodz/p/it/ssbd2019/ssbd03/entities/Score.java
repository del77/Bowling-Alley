package pl.lodz.p.it.ssbd2019.ssbd03.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Klasa reprezentująca wyniki.
 */
@Entity
@Table(name = "scores", schema = "public", catalog = "ssbd03")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Score {
    @Id
    @NotNull
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    @EqualsAndHashCode.Exclude
    private Long id;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "reservation_id",
            updatable = false,
            nullable = false,
            foreignKey = @ForeignKey(name = "fk__score__reservation", value = ConstraintMode.CONSTRAINT))
    private Reservation reservation;

    @ManyToOne(cascade=CascadeType.REFRESH)
    @NotNull
    @JoinColumn(name = "user_id",
            updatable = false,
            nullable = false,
            foreignKey = @ForeignKey(name = "fk__score__user", value = ConstraintMode.CONSTRAINT))
    private UserAccount userAccount;


    @Column(name = "score", nullable = false)
    @NotNull
    @Max(300)
    @Min(0)
    private int score;

    @Version
    @Min(0)
    @NotNull
    @Column(name = "version", nullable = false)
    private long version;
}
