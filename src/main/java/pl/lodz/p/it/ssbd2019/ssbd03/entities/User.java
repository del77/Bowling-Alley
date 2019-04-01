package pl.lodz.p.it.ssbd2019.ssbd03.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "users", schema = "public", catalog = "ssbd03")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private int id;

    @Column(name = "first_name", nullable = false, length = 16)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 16)
    private String lastName;

    @Column(name = "phone", nullable = false, length = 16)
    private String phone;

    @Column(name = "birth_date", nullable = false)
    private Date birthDate;

    @Column(name = "version", nullable = false)
    private int version;

    @Column(name = "email", nullable = false, length = 25)
    private String email;

    @OneToOne
    @JoinColumn(name = "id")
    @MapsId
    private Account account;
}
