package pl.lodz.p.it.ssbd2019.ssbd03.entities;

import lombok.*;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Klasa reprezentująca rekord w widoku glassfish_auth_view
 */
@Data
@IdClass(AuthenticationViewEntityId.class)
@Entity
@Immutable
@Table(name = "glassfish_auth_view", schema = "public", catalog = "ssbd03")
public class AuthenticationViewEntity {
    
    @Id
    @NotEmpty
    @NotNull
    @Size(max = 16)
    @Column(name = "login", nullable = false, length = 16)
    private String login;
    
    @NotEmpty
    @NotNull
    @Size(min = 64, max = 64)
    @Column(name = "password", nullable = false, length = 64)
    @ToString.Exclude
    private String password;
    
    @Id
    @NotEmpty
    @NotNull
    @Size(max = 16)
    @Column(name = "name", nullable = false, length = 16)
    private String accessLevelName;
}
