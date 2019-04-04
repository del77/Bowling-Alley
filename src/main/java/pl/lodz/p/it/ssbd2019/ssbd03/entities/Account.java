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
    @SequenceGenerator(name = "AccountSeqGen",
            sequenceName = "AccountSequence",
            initialValue = 1,
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AccountSeqGen")
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    @EqualsAndHashCode.Exclude
    private int id;

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

    @Min(0)
    @Column(name = "version", nullable = false)
    private int version;

    @OneToOne(mappedBy = "account")
    private User user;
}
