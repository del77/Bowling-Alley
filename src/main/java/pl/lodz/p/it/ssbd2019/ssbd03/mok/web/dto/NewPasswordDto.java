package pl.lodz.p.it.ssbd2019.ssbd03.mok.web.dto;

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

    @NotNull(message = "{validate.passwordNotNull}")
    @NotBlank(message = "{validate.passwordNotBlank}")
    @Size(min = 8, message = "{validate.passwordAtLeastEight}")
    @Size(max = 64, message = "{validate.passwordShorterThanSixtyFour}")
    @FormParam("newPassword")
    String newPassword;

    @NotNull(message = "{validate.passwordConfirmNotNull}")
    @NotBlank(message = "{validate.passwordConfirmNotBlank}")
    @FormParam("confirmNewPassword")
    String confirmNewPassword;
}
