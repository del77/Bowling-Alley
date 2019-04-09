package pl.lodz.p.it.ssbd2019.ssbd03.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "access_levels", schema = "public", catalog = "ssbd03")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccessLevel {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    @EqualsAndHashCode.Exclude
    private long id;

    @NotEmpty
    @Column(name = "name", nullable = false, length = 16, unique = true)
    private String name;

    @Min(0)
    @Column(name = "version", nullable = false)
    private int version;
}
