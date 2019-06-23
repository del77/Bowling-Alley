package pl.lodz.p.it.ssbd2019.ssbd03.mor.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.ws.rs.FormParam;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationItemsDto {
    
    @FormParam("size")
    private List<@Min(1) Integer> size;
    
    @FormParam("count")
    private List<@Min(1) Integer> count;
}
