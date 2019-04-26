package pl.lodz.p.it.ssbd2019.ssbd03.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
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
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    @EqualsAndHashCode.Exclude
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reservation_id",
            updatable = false,
            nullable = false,
            foreignKey = @ForeignKey(name = "fk__score__reservation", value = ConstraintMode.CONSTRAINT))
    private Reservation reservation;

    @ManyToOne(cascade=CascadeType.REFRESH)
    @JoinColumn(name = "user_id",
            updatable = false,
            nullable = false,
            foreignKey = @ForeignKey(name = "fk__score__user", value = ConstraintMode.CONSTRAINT))
    private UserAccount userAccount;


    @Column(name = "score", nullable = false)
    @Max(300)
    @Min(0)
    private int score;

    @Version
    @Min(0)
    @Column(name = "version", nullable = false)
    private long version;
}
