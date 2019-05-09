package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto;

import lombok.*;

import javax.ws.rs.FormParam;

@NoArgsConstructor
@ToString
@Getter
@Setter
@EqualsAndHashCode
public class AccountActivationDto {
    @FormParam("accountId")
    private Long id;
    
    @FormParam("activeToggle")
    private String active;
}
