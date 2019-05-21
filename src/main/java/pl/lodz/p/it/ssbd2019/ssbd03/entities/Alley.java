package pl.lodz.p.it.ssbd2019.ssbd03.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Klasa reprezentująca tory.
 */
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
    private Long id;

    @Min(0)
    @NotNull
    @Column(name = "number", nullable = false, unique = true)
    @ToString.Exclude
    private int number;
    
    @NotNull
    @Column(name = "active", nullable = false)
    @ToString.Exclude
    private boolean active;
    
    @NotNull
    @Column(name = "max_score", nullable = false)
    @Min(0)
    @Max(300)
    @ToString.Exclude
    private int maxScore;

    @Version
    @NotNull
    @Min(0)
    @Column(name = "version", nullable = false)
    private long version;
}
