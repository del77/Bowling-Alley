package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.FormParam;

/**
 * Klasa reprezentująca dane z formularza rejestracji.
 */
@Data
public class UserAccountDto {
    @NotNull(message = "Login cannot be null.")
    @NotBlank(message = "Login cannot be blank.")
    @FormParam("login")
    String login;
    @NotNull(message = "First name cannot be null.")
    @NotBlank(message = "First name cannot be blank.")
    @FormParam("firstName")
    String firstName;
    @NotNull(message = "Last name cannot be null.")
    @NotBlank(message = "Last name cannot be blank.")
    @FormParam("lastName")
    String lastName;
    @NotNull(message = "E-mail cannot be null.")
    @NotBlank(message = "E-mail cannot be blank.")
    @Email(message = "E-mail should be valid.")
    @FormParam("email")
    String email;
    @NotNull(message = "Password cannot be null.")
    @NotBlank(message = "Password cannot be blank.")
    @FormParam("password")
    String password;
    @NotNull(message = "Password confirmation cannot be null.")
    @NotBlank(message = "Password confirmation cannot be blank.")
    @FormParam("confirmPassword")
    String confirmPassword;
    @NotNull(message = "Phone number cannot be null.")
    @NotBlank(message = "Phone number cannot be blank.")
    @Size(min = 9, message = "Phone needs to be at least 9 characters long.")
    @FormParam("phoneNumber")
    String phoneNumber;
}
