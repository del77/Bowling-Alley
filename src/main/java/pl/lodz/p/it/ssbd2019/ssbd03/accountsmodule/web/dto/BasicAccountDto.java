package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto;

import lombok.*;
import pl.lodz.p.it.ssbd2019.ssbd03.validators.ValidPhoneNumberFormat;

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

    @NotNull(message = "{validate.loginNotNull}")
    @NotBlank(message = "{validate.loginNotBlank}")
    @Size(max = 16, message = "{validate.loginShorterThanSixteen}")
    @FormParam("login")
    protected String login;

    @NotNull(message = "{validate.firstNameNotNull}")
    @NotBlank(message = "{validate.firstNameNotBlank}")
    @Size(max = 32, message = "{validate.firstNameShorterThanThirtyTwo}")
    @FormParam("firstName")
    protected String firstName;

    @NotNull(message = "{validate.lastNameNotNull}")
    @NotBlank(message = "{validate.lastNameNotBlank}")
    @Size(max = 32, message = "{validate.lastNameShorterThanThirtyTwo}")
    @FormParam("lastName")
    protected String lastName;

    @NotNull(message = "{validate.emailNotNull}")
    @NotBlank(message = "{validate.emailNotBlank}")
    @Email(message = "{validate.emailValid}")
    @Size(max = 50, message = "{validate.emailShorterThanFifty}")
    @FormParam("email")
    protected String email;

    @NotNull(message = "{validate.passwordNotNull}")
    @NotBlank(message = "{validate.passwordNotBlank}")
    @Size(min = 8, message = "{validate.passwordAtLeastEight}")
    @Size(max = 64, message = "{validate.passwordShorterThanSixtyFour}")
    @FormParam("password")
    protected String password;

    @NotNull(message = "{validate.passwordConfirmNotNull}")
    @NotBlank(message = "{validate.passwordConfirmNotBlank}")
    @FormParam("confirmPassword")
    protected String confirmPassword;

    @NotNull(message = "{validate.phoneNumberNotNull}")
    @NotBlank(message = "{validate.phoneNumberNotBlank}")
    @Size(min = 9, message = "{validate.phoneNumberAtLeastNine}")
    @Size(max = 16, message = "{validate.phoneNumberShorterThanSixteen}")
    @ValidPhoneNumberFormat
    @FormParam("phoneNumber")
    protected String phoneNumber;

}
