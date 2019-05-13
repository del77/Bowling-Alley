package pl.lodz.p.it.ssbd2019.ssbd03.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.sql.Timestamp;

@Entity
@Table(name = "reset_password_tokens", schema = "public", catalog = "ssbd03")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@NamedQueries(
        value = {
                @NamedQuery(name = "ResetPasswordToken.findByToken", query = "select a from ResetPasswordToken a where a.token = :token"),
        }
)
public class ResetPasswordToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    @EqualsAndHashCode.Exclude
    private Long id;

    @Column(name = "validity", nullable = false)
    private Timestamp validity;

    @Column(name = "token", nullable = false)
    private String token;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk__resetpasswordtokens__users", value = ConstraintMode.CONSTRAINT)
    )
    private UserAccount userAccount;

    @Version
    @Min(0)
    @Column(name = "version", nullable = false)
    private long version;
}
