package pl.lodz.p.it.ssbd2019.ssbd03.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "accounts_accesses", schema = "public", catalog = "ssbd03")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(AccountAccessLevelId.class)
public class AccountAccessLevel {
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private Account account;

    @Id
    @ManyToOne
    @JoinColumn(name = "access_level_id")
    private AccessLevel accessLevel;

    @Column(name = "active", nullable = false)
    private boolean active;

    @Column(name = "version", nullable = false)
    private int version;
}
