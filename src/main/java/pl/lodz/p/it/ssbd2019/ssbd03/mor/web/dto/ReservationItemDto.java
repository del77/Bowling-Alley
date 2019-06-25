package pl.lodz.p.it.ssbd2019.ssbd03.mor.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.ws.rs.FormParam;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationItemDto {
    
    @FormParam("size")
    @Min(1)
    private int size;
    
    @FormParam("count")
    @Min(1)
    private int count;
}
