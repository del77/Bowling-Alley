package pl.lodz.p.it.ssbd2019.ssbd03.mor.web.dto.newReservation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.FormParam;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ShoesDto {

    @Min(6)
    @Max(16)
    @FormParam("size")
    private int size;

    @FormParam("number")
    private Integer number;

}
