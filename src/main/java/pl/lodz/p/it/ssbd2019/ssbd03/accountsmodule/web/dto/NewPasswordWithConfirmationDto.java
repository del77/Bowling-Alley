package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.ws.rs.FormParam;

/**
 * Klasa reprezentująca dane z formularza edytcji hasła przez użytkownika.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class NewPasswordWithConfirmationDto extends NewPasswordDto {
    @NotNull(message = "Current password cannot be null.")
    @NotBlank(message = "Current password cannot be blank.")
    @FormParam("currentPassword")
    String currentPassword;


}
