package pl.lodz.p.it.ssbd2019.ssbd03.mot.web.dto;


import lombok.*;

import javax.validation.constraints.Min;
import javax.ws.rs.FormParam;


@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ItemDto {
    @FormParam("sizes")
    @Min(value = 0, message = "{validate.itemSizeMin}")
    private int size;

    @FormParam("counts")
    @Min(value = 0, message = "{validate.itemCountMin}")
    private int count;
}
