package pl.lodz.p.it.ssbd2019.ssbd03.mot.web.dto;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.ws.rs.FormParam;

@Data
@NoArgsConstructor
public class AlleyCreationDto {

    @NotNull(message = "{validate.alleyNumberIsNull}")
    @Min(value = 1, message = "{validate.alleyNumberConstraint}")
    @Max(value = 2147483647, message = "{validate.alleyNumberTooBigConstraint}")
    @FormParam("number")
    private int number;

}
