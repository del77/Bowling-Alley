package pl.lodz.p.it.ssbd2019.ssbd03.mot.web.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.ws.rs.FormParam;

@Data
@NoArgsConstructor
public class AlleyLockDto {
    @FormParam("alleyId")
    private Long id;

    @FormParam("activeToggle")
    private Boolean active;
}
