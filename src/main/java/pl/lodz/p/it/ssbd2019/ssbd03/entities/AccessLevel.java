package pl.lodz.p.it.ssbd2019.ssbd03.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * Klasa reprezentująca poziom dostępu.
 */
@Entity
@Table(name = "access_levels", schema = "public", catalog = "ssbd03")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@NamedQueries(
        @NamedQuery(
                name = "AccessLevel.findByName",
                query = "select al from AccessLevel al where al.name = :name"
        )
)
public class AccessLevel {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    @EqualsAndHashCode.Exclude
    private Long id;

    @NotEmpty
    @NotNull
    @Column(name = "name", nullable = false, length = 16, unique = true)
    private String name;

    @Version
    @Min(0)
    @NotNull
    @Column(name = "version", nullable = false)
    private long version;
}
