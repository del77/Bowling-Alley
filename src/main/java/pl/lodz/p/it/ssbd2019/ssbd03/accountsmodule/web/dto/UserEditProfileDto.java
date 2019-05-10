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

    @NotNull(message = "First name cannot be null.")
    @NotBlank(message = "First name cannot be blank.")
    @FormParam("firstName")
    String firstName;

    @NotNull(message = "Last name cannot be null.")
    @NotBlank(message = "Last name cannot be blank.")
    @FormParam("lastName")
    String lastName;

    @NotNull(message = "Email cannot be null.")
    @NotBlank(message = "Email cannot be blank.")
    @Email(message = "Invalid e-mail.")
    @FormParam("eMail")
    String email;

    @NotNull(message = "Phone number cannot be null.")
    @NotBlank(message = "Phone number cannot be blank.")
    @FormParam("phoneNumber")
    String phoneNumber;
}