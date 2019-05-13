package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.FormParam;

@Data
public class EmailDto {
    @NotNull(message = "E-mail cannot be null.")
    @NotBlank(message = "E-mail cannot be blank.")
    @Email(message = "E-mail should be valid.")
    @Size(max = 50, message = "Email cannot be longer than 50 characters.")
    @FormParam("email")
    protected String email;
}
