package pl.lodz.p.it.ssbd2019.ssbd03.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Klasa reprezentująca klucz dla encji z widoku glassfish_auth_view.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationViewEntityId implements Serializable {
    private static final long serialVersionUID = 1L;
    private String login;
    private String accessLevelName;
}
