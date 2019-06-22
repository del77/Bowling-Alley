package pl.lodz.p.it.ssbd2019.ssbd03.mor.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.FormParam;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeNewReservationDto extends ClientNewReservationDto {

    @NotNull(message = "{validate.loginNotNull}")
    @NotBlank(message = "{validate.loginNotBlank}")
    @Size(max = 16, message = "{validate.loginShorterThanSixteen}")
    @FormParam("userLogin")
    protected String userLogin;

}
