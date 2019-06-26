package pl.lodz.p.it.ssbd2019.ssbd03.mok.web.dto;

import lombok.*;
import pl.lodz.p.it.ssbd2019.ssbd03.validators.PhoneNumberFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.FormParam;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class AccountDetailsDto {
    protected Long id;

    @Size(max = 16, message = "{validate.loginShorterThanSixteen}")
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
    @FormParam("email")
    @Size(max = 50, message = "{validate.emailShorterThanFifty}")
    protected String email;
    
    @NotNull(message = "{validate.phoneNumberNotNull}")
    @NotBlank(message = "{validate.phoneNumberNotBlank}")
    @Size(min = 9, message = "{validate.phoneNumberAtLeastNine}")
    @Size(max = 16, message = "{validate.phoneNumberShorterThanSixteen}")
    @PhoneNumberFormat(message = "{validate.phoneNumberWrongFormat}")
    @FormParam("phoneNumber")
    protected String phone;

    @FormParam("clientSelected")
    boolean clientRoleSelected;

    @FormParam("employeeSelected")
    boolean employeeRoleSelected;

    @FormParam("adminSelected")
    boolean adminRoleSelected;

    boolean accountActive;

    boolean confirmed = true;

    @FormParam("g-recaptcha-response")
    protected String recaptcha;
}
