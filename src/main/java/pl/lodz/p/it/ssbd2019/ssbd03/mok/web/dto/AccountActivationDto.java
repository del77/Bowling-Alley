package pl.lodz.p.it.ssbd2019.ssbd03.mok.web.dto;

import lombok.*;

import javax.ws.rs.FormParam;

@NoArgsConstructor
@Data
public class AccountActivationDto {
    @FormParam("accountId")
    private Long id;
    
    @FormParam("activeToggle")
    private Boolean active;
}
