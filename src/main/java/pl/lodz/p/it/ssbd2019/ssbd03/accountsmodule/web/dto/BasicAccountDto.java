package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto;

import lombok.*;

import javax.validation.constraints.*;
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
    @Size(max = 16, message = "Login cannot be longer than 16 characters.")
    @FormParam("login")
    protected String login;

    @NotNull(message = "First name cannot be null.")
    @NotBlank(message = "First name cannot be blank.")
    @Size(max = 32, message = "First name cannot be longer than 32 characters.")
    @FormParam("firstName")
    protected String firstName;

    @NotNull(message = "Last name cannot be null.")
    @NotBlank(message = "Last name cannot be blank.")
    @Size(max = 32, message = "Last name cannot be longer than 32 characters.")
    @FormParam("lastName")
    protected String lastName;

    @NotNull(message = "E-mail cannot be null.")
    @NotBlank(message = "E-mail cannot be blank.")
    @Email(message = "E-mail should be valid.")
    @Size(max = 50, message = "Email cannot be longer than 50 characters.")
    @FormParam("email")
    protected String email;

    @NotNull(message = "{validate.passwordNotNull}")
    @NotBlank(message = "{validate.passwordNotBlank}")
    @Size(min = 8, message = "Password must contain at least 8 characters.")
    @Size(max = 64, message = "Password cannot be longer than 64 characters.")
    @FormParam("password")
    protected String password;

    @NotNull(message = "Password confirmation cannot be null.")
    @NotBlank(message = "Password confirmation cannot be blank.")
    @FormParam("confirmPassword")
    protected String confirmPassword;

    @NotNull(message = "Phone number cannot be null.")
    @NotBlank(message = "Phone number cannot be blank.")
    @Size(min = 9, message = "Phone needs to be at least 9 characters long.")
    @Size(max = 16, message = "Phone cannot be longer than 16 characters.")
    @FormParam("phoneNumber")
    protected String phoneNumber;

}
