package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import pl.lodz.p.it.ssbd2019.ssbd03.validators.PhoneNumberFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.FormParam;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEditProfileDto {

    @NotNull(message = "{validate.firstNameNotNull}")
    @NotBlank(message = "{validate.firstNameNotBlank}")
    @FormParam("firstName")
    @Size(max = 32, message = "{validate.firstNameShorterThanThirtyTwo}")
    String firstName;

    @NotNull(message = "{validate.lastNameNotNull}")
    @NotBlank(message = "{validate.lastNameNotBlank}")
    @Size(max = 32, message = "{validate.lastNameShorterThanThirtyTwo}")
    @FormParam("lastName")
    String lastName;

    @NotNull(message = "{validate.emailNotNull}")
    @NotBlank(message = "{validate.emailNotBlank}")
    @Email(message = "{validate.emailValid}")
    @Size(max = 50, message = "{validate.emailShorterThanFifty}")
    @FormParam("eMail")
    String email;

    @NotNull(message = "{validate.phoneNumberNotNull}")
    @NotBlank(message = "{validate.phoneNumberNotBlank}")
    @Size(min = 9, message = "{validate.phoneNumberAtLeastNine}")
    @Size(max = 16, message = "{validate.phoneNumberShorterThanSixteen}")
    @PhoneNumberFormat(message = "{validate.phoneNumberWrongFormat}")
    @FormParam("phoneNumber")
    String phoneNumber;
}