package pl.lodz.p.it.ssbd2019.ssbd03.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Entity
@Table(name = "accounts", schema = "public", catalog = "ssbd03")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    @EqualsAndHashCode.Exclude
    private long id;

    @NotEmpty
    @Column(name = "login", nullable = false, length = 16, unique = true)
    private String login;

    @NotEmpty
    @Column(name = "password", nullable = false, length = 64)
    private String password;

    @Column(name = "confirmed", nullable = false)
    private boolean confirmed;

    @Column(name = "active", nullable = false)
    private boolean active;

    @Version
    @Min(0)
    @Column(name = "version", nullable = false)
    private long version;

    @OneToOne(mappedBy = "account", cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
    private User user;
}
