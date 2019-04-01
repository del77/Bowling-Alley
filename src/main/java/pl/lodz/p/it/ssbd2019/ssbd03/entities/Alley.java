package pl.lodz.p.it.ssbd2019.ssbd03.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;

@Entity
@Table(name = "alleys", schema = "public", catalog = "ssbd03")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Alley {
    @Id
    @SequenceGenerator(name = "AlleySeqGen", sequenceName = "AlleySequence", initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AlleySeqGen")
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    @EqualsAndHashCode.Exclude
    private int id;

    @Min(0)
    @Column(name = "number", nullable = false, unique = true)
    private int number;

    @Column(name = "active", nullable = false)
    private boolean active;

    @Min(0)
    @Column(name = "version", nullable = false)
    private int version;
}
