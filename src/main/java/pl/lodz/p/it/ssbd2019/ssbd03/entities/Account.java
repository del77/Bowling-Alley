package pl.lodz.p.it.ssbd2019.ssbd03.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "accounts", schema = "public", catalog = "ssbd03")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, updatable = false)
    private int id;

    @Column(name = "login", nullable = false, length = 16)
    private String login;

    @Column(name = "password", nullable = false, length = 64)
    private String password;

    @Column(name = "confirmed", nullable = false)
    private boolean confirmed;

    @Column(name = "active", nullable = false)
    private boolean active;

    @Column(name = "version", nullable = false)
    private int version;

    @OneToMany(mappedBy = "account")
    private List<AccountAccessLevel> accountAccessLevels;

    @OneToOne(mappedBy = "account")
    private User user;
}
