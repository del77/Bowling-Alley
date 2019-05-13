package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.ws.rs.FormParam;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEditProfileDto {

    @NotNull(message = "{validate.firstNameNotNull}")
    @NotBlank(message = "{validate.firstNameNotBlank}")
    @FormParam("firstName")
    String firstName;

    @NotNull(message = "{validate.lastNameNotNull}")
    @NotBlank(message = "{validate.lastNameNotBlank}")
    @FormParam("lastName")
    String lastName;

    @NotNull(message = "{validate.emailNotNull}")
    @NotBlank(message = "{validate.emailNotBlank}")
    @Email(message = "{validate.emailValid}")
    @FormParam("eMail")
    String email;

    @NotNull(message = "{validate.phoneNumberNotNull}")
    @NotBlank(message = "{validate.phoneNumberNotBlank}")
    @FormParam("phoneNumber")
    String phoneNumber;
}