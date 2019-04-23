package pl.lodz.p.it.ssbd2019.ssbd03.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * Klasa reprezentująca dane użytkowników.
 */
@Entity
@Table(name = "users", schema = "public", catalog = "ssbd03")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    @EqualsAndHashCode.Exclude
    private Long id;

    @NotEmpty
    @Column(name = "first_name", nullable = false, length = 32)
    private String firstName;

    @NotEmpty
    @Column(name = "last_name", nullable = false, length = 32)
    private String lastName;

    @Size(min = 9)
    @NotNull
    @Column(name = "phone", length = 16)
    private String phone;

    @Version
    @Min(0)
    @Column(name = "version", nullable = false)
    private long version;

    @Email
    @Column(name = "email", nullable = false, length = 50, unique = true)
    private String email;

    @OneToOne
    @JoinColumn(name = "id",
            unique = true,
            updatable = false,
            nullable = false,
            foreignKey = @ForeignKey(name = "fk__user__account", value = ConstraintMode.CONSTRAINT))
    @MapsId
    private Account account;
}
