package pl.lodz.p.it.ssbd2019.ssbd03.mor.web.dto;

import javax.ws.rs.FormParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AvailableAlleyDto {

    private Long id;
    
    @FormParam("alleyNumber")
    private int alleyNumber;
    
}
