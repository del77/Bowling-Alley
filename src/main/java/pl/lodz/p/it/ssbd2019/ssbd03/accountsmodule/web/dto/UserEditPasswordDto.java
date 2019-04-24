package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.FormParam;

/**
 * Klasa reprezentująca dane z formularza edytcji hasła.
 */
@Data
public class UserEditPasswordDto {
    @NotNull(message = "Current password cannot be null.")
    @NotBlank(message = "Current password cannot be blank.")
    @FormParam("currentPassword")
    String currentPassword;

    @NotNull(message = "New password cannot be null.")
    @NotBlank(message = "New password cannot be blank.")
    @FormParam("newPassword")
    String newPassword;

    @NotNull(message = "Password confirmation cannot be null.")
    @NotBlank(message = "Password confirmation cannot be blank.")
    @FormParam("confirmNewPassword")
    String confirmNewPassword;
}
