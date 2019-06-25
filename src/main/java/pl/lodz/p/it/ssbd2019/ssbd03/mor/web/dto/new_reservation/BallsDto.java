package pl.lodz.p.it.ssbd2019.ssbd03.mor.web.dto.new_reservation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.FormParam;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BallsDto {

    @Min(value = 6, message = "{validate.itemSizeMin}")
    @Max(value = 16, message = "{validate.itemSizeMax}")
    @FormParam("size")
    private int size;

    @FormParam("number")
    private Integer number;

}