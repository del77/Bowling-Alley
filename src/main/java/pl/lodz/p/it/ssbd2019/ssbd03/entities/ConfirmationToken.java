package pl.lodz.p.it.ssbd2019.ssbd03.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Klasa reprezentująca wyniki.
 */
@Entity
@Table(name = "confirmation_tokens", schema = "public", catalog = "ssbd03")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@NamedQueries(
        value = {
                @NamedQuery(
                        name = "ConfirmationToken.findByToken",
                        query = "select t from ConfirmationToken t where t.token = :token"
                )
        }
)
public class ConfirmationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    @EqualsAndHashCode.Exclude
    private Long id;

    @Column(name = "token", nullable = false)
    private String token;

    @NotNull
    @OneToOne(
            cascade = { CascadeType.REFRESH, CascadeType.MERGE },
            fetch = FetchType.EAGER
    )
    @JoinColumn(name = "user_id",
            updatable = false,
            nullable = false,
            foreignKey = @ForeignKey(name = "fk__ct__user", value = ConstraintMode.CONSTRAINT))
    @ToString.Exclude
    private UserAccount userAccount;

    @Version
    @Min(0)
    @NotNull
    @Column(name = "version", nullable = false)
    private long version;
}
