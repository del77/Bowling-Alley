package pl.lodz.p.it.ssbd2019.ssbd03.mok.web.dto;

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

    @NotNull(message = "{validate.currentPasswordNotNull}")
    @NotBlank(message = "{validate.currentPasswordNotBlank}")
    @FormParam("currentPassword")
    String currentPassword;

    @FormParam("g-recaptcha-response")
    protected String recaptcha;
}
