package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.FormParam;

/**
 * Klasa reprezentująca dane z formularza edytcji hasła przez administratora.
 */
@Data
public class NewPasswordDto {

    @NotNull(message = "New password cannot be null.")
    @NotBlank(message = "New password cannot be blank.")
    @Size(max = 64, message = "Password cannot be longer than 64 characters.")
    @Size(min = 8, message = "Password cannot be shorter than 8 characters.")
    @FormParam("newPassword")
    String newPassword;

    @NotNull(message = "Password confirmation cannot be null.")
    @NotBlank(message = "Password confirmation cannot be blank.")
    @FormParam("confirmNewPassword")
    String confirmNewPassword;
}
