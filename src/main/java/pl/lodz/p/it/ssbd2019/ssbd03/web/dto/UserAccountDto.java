package pl.lodz.p.it.ssbd2019.ssbd03.web.dto;

import lombok.Data;

import javax.ws.rs.FormParam;

/**
 * Klasa reprezentująca dane z formularza rejestracji.
 */
@Data
public class UserAccountDto {
    @FormParam("login")
    String login;
    @FormParam("firstName")
    String firstName;
    @FormParam("lastName")
    String lastName;
    @FormParam("email")
    String email;
    @FormParam("password")
    String password;
    @FormParam("confirmPassword")
    String confirmPassword;
    @FormParam("phoneNumber")
    String phoneNumber;
}
