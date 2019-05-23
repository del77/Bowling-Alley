package pl.lodz.p.it.ssbd2019.ssbd03.entities;

import lombok.*;
import pl.lodz.p.it.ssbd2019.ssbd03.validators.PhoneNumberFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;

/**
 * Klasa reprezentująca dane użytkowników.
 */
@Entity
@Table(name = "accounts", schema = "public", catalog = "ssbd03")
@SecondaryTable(name = "users", pkJoinColumns = @PrimaryKeyJoinColumn(name = "id"),
        schema = "public", catalog = "ssbd03")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@NamedQueries(
        value = {
                @NamedQuery(name = "UserAccount.findByLogin", query = "select a from UserAccount a where a.login = :login"),
                @NamedQuery(name = "UserAccount.findByEmail", query = "select a from UserAccount a where a.email = :email"),
        }
)
public class UserAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    @EqualsAndHashCode.Exclude
    private Long id;

    @NotEmpty
    @NotNull
    @Size(max = 32)
    @Column(name = "first_name", nullable = false, length = 32, table = "users")
    @ToString.Exclude
    private String firstName;

    @NotEmpty
    @NotNull
    @Size(max = 32)
    @Column(name = "last_name", nullable = false, length = 32, table = "users")
    @ToString.Exclude
    private String lastName;

    @Size(min = 9, max = 16)
    @NotNull
    @PhoneNumberFormat
    @Column(name = "phone", nullable = false, length = 16, table = "users")
    @ToString.Exclude
    private String phone;

    @Email
    @NotNull
    @Size(max = 50)
    @Column(name = "email", nullable = false, length = 50, unique = true, table = "users")
    @ToString.Exclude
    private String email;

    @NotEmpty
    @NotNull
    @Size(max = 16)
    @Column(name = "login", nullable = false, length = 16, unique = true)
    private String login;

    @NotEmpty
    @NotNull
    @Size(min = 64, max = 64)
    @Column(name = "password", nullable = false, length = 64)
    @ToString.Exclude
    private String password;

    @NotNull
    @Column(name = "confirmed", nullable = false)
    @ToString.Exclude
    private boolean accountConfirmed;

    @NotNull
    @Column(name = "active", nullable = false)
    @ToString.Exclude
    private boolean accountActive;
    
    @Column(name="failed_logins_counter")
    private Integer failedLoginsCounter;

    /**
     * Ta lista tworzy rekurencyjną relację - w przypadku odczytywaniu z bazy nie ma problemu,
     * ale adnotacja @NotNull nie pozwala utworzyć AccountAccessLevels bez UserAccount i odwrotnie,
     * co skutecznie uniemożliwia tworzenie nowych encji.
     */
    @OneToMany(mappedBy = "account", cascade = {CascadeType.REFRESH, CascadeType.MERGE, CascadeType.PERSIST})
    @ToString.Exclude
    private List<AccountAccessLevel> accountAccessLevels;
    
    @OneToMany(cascade={CascadeType.MERGE})
    @JoinColumn(name="user_id", referencedColumnName = "id", nullable = false)
    private List<PreviousUserPassword> previousUserPasswords;

    @Version
    @NotNull
    @Min(0)
    @Column(name = "version", nullable = false)
    private Long version;
}
