package pl.lodz.p.it.ssbd2019.ssbd03.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;

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
    private long id;

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
    private User user;

    @Min(0)
    @Column(name = "score", nullable = false)
    private int score;

    @Min(0)
    @Column(name = "version", nullable = false)
    private int version;
}
