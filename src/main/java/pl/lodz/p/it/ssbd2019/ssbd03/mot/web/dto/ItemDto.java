package pl.lodz.p.it.ssbd2019.ssbd03.mot.web.dto;


import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.FormParam;


@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ItemDto {
    @FormParam("size")
    @Min(value = 0, message = "{validate.itemSizeMin}")
    @Max(value = Long.MAX_VALUE, message = "{validate.itemSizeMax}" )
    private int size;

    @FormParam("count")
    @Min(value = -10, message = "{validate.itemCountMin}")
    @Max(value = Long.MAX_VALUE, message = "{validate.itemCountMax}")
    private int count;
}
