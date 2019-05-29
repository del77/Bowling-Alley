package pl.lodz.p.it.ssbd2019.ssbd03.mok.web.dto;

import lombok.*;

import javax.ws.rs.FormParam;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class UserRolesDto implements AccessLevelsSelection {

    @FormParam("clientSelected")
    boolean clientRoleSelected;
    
    @FormParam("employeeSelected")
    boolean employeeRoleSelected;
    
    @FormParam("adminSelected")
    boolean adminRoleSelected;
    
}