package pl.lodz.p.it.ssbd2019.ssbd03.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Entity
@Table(name = "alleys", schema = "public", catalog = "ssbd03")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Alley {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    @EqualsAndHashCode.Exclude
    private long id;

    @Min(0)
    @Column(name = "number", nullable = false, unique = true)
    private int number;

    @Column(name = "active", nullable = false)
    private boolean active;

    @Column(name = "max_score", nullable = true)
    @Min(0)
    @Max(300)
    private Integer max_score;

    @Version
    @Min(0)
    @Column(name = "version", nullable = false)
    private long version;
}
