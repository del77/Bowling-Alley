package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.FormParam;

/**
 * Klasa reprezentująca dane z formularza rejestracji.
 */
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class BasicAccountDto {

    @NotNull(message = "Login cannot be null.")
    @NotBlank(message = "Login cannot be blank.")
    @FormParam("login")
    protected String login;

    @NotNull(message = "First name cannot be null.")
    @NotBlank(message = "First name cannot be blank.")
    @FormParam("firstName")
    protected String firstName;

    @NotNull(message = "Last name cannot be null.")
    @NotBlank(message = "Last name cannot be blank.")
    @FormParam("lastName")
    protected String lastName;

    @NotNull(message = "E-mail cannot be null.")
    @NotBlank(message = "E-mail cannot be blank.")
    @Email(message = "E-mail should be valid.")
    @FormParam("email")
    protected String email;

    @NotNull(message = "Password cannot be null.")
    @NotBlank(message = "Password cannot be blank.")
    @FormParam("password")
    protected String password;

    @NotNull(message = "Password confirmation cannot be null.")
    @NotBlank(message = "Password confirmation cannot be blank.")
    @FormParam("confirmPassword")
    protected String confirmPassword;

    @NotNull(message = "Phone number cannot be null.")
    @NotBlank(message = "Phone number cannot be blank.")
    @Size(min = 9, message = "Phone needs to be at least 9 characters long.")
    @FormParam("phoneNumber")
    protected String phoneNumber;

}
