package pl.lodz.p.it.ssbd2019.ssbd03.entities;

import lombok.*;

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
                @NamedQuery(name = "UserAccount.findByLogin",
                        query = "select a from UserAccount a where a.login = :login"),
        }
)
public class UserAccount {
    @Id
    @NotNull
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    @EqualsAndHashCode.Exclude
    private Long id;

    @NotEmpty
    @NotNull
    @Column(name = "first_name", nullable = false, length = 32, table = "users")
    private String firstName;

    @NotEmpty
    @NotNull
    @Column(name = "last_name", nullable = false, length = 32, table = "users")
    private String lastName;

    @Size(min = 9)
    @NotNull
    @Column(name = "phone", nullable = false, length = 16, table = "users")
    private String phone;

    @Email
    @NotNull
    @Column(name = "email", nullable = false, length = 50, unique = true, table = "users")
    private String email;

    @NotEmpty
    @NotNull
    @Column(name = "login", nullable = false, length = 16, unique = true)
    private String login;

    @NotEmpty
    @NotNull
    @Column(name = "password", nullable = false, length = 64)
    private String password;
    
    @NotNull
    @Column(name = "confirmed", nullable = false)
    private boolean accountConfirmed;
    
    @NotNull
    @Column(name = "active", nullable = false)
    private boolean accountActive;
    
    @NotNull
    @OneToMany(mappedBy = "account", cascade={CascadeType.REFRESH, CascadeType.MERGE, CascadeType.PERSIST})
    private List<AccountAccessLevel> accountAccessLevels;

    @Version
    @NotNull
    @Min(0)
    @Column(name = "version", nullable = false)
    private Long version;
}
