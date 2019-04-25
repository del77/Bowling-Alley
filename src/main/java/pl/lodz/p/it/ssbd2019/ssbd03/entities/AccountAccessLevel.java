package pl.lodz.p.it.ssbd2019.ssbd03.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;

/**
 * Klasa reprezentująca relację wiele do wielu pomiedzy kontem a poziomem dostępu.
 */
@Entity
@Table(name = "accounts_accesses", schema = "public", catalog = "ssbd03")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(AccountAccessLevelId.class)
@NamedQueries(
        value = {
                @NamedQuery(name = "AccountAccessLevel.findForAccountId",
                        query = "select a from AccountAccessLevel a where a.account = :account"),
                @NamedQuery(name = "AccountAccessLevel.findForAccessLevelId",
                        query = "select a from AccountAccessLevel a where a.accessLevel = :access"),
                @NamedQuery(name = "AccountAccessLevel.findForAccountIdAndAccessLevelId",
                        query = "select a from AccountAccessLevel a where a.accessLevel = :access AND a.account = :account")
        }
)
public class AccountAccessLevel {
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id",
            unique = true,
            updatable = false,
            nullable = false,
            foreignKey = @ForeignKey(name = "fk__accounts_accesses__account",
                    value = ConstraintMode.CONSTRAINT))
    private Account account;

    @Id
    @ManyToOne
    @JoinColumn(name = "access_level_id",
            unique = true,
            updatable = false,
            nullable = false,
            foreignKey = @ForeignKey(name = "fk__accounts_accesses__access_level",
                    value = ConstraintMode.CONSTRAINT))
    private AccessLevel accessLevel;

    @Column(name = "active", nullable = false)
    private boolean active;

    @Version
    @Min(0)
    @Column(name = "version", nullable = false)
    private long version;
}
