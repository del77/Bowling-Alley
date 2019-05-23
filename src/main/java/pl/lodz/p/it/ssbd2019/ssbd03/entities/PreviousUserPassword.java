package pl.lodz.p.it.ssbd2019.ssbd03.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "previous_users_passwords", schema = "public", catalog = "ssbd03")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PreviousUserPassword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    @EqualsAndHashCode.Exclude
    private Long id;
    
    @NotEmpty
    @NotNull
    @Size(min = 64, max = 64)
    @Column(name = "password", nullable = false, length = 64)
    @ToString.Exclude
    private String password;
    
    @Version
    @Min(0)
    @NotNull
    @Column(name = "version", nullable = false)
    private long version;
}