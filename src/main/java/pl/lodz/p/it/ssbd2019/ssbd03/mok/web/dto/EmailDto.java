package pl.lodz.p.it.ssbd2019.ssbd03.mok.web.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.FormParam;

@Data
public class EmailDto {
    @NotNull(message = "{validate.emailNotNull}")
    @NotBlank(message = "{validate.emailNotBlank}")
    @Email(message = "{validate.emailValid}")
    @Size(max = 50, message = "{validate.emailShorterThanFifty}")
    @FormParam("email")
    protected String email;
}
