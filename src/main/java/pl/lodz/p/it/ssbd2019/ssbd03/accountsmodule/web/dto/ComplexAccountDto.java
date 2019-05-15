package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.ws.rs.FormParam;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@NoArgsConstructor
public class ComplexAccountDto extends BasicAccountDto {

    @FormParam("clientSelected")
    boolean clientRoleSelected;

    @FormParam("employeeSelected")
    boolean employeeRoleSelected;

    @FormParam("adminSelected")
    boolean adminRoleSelected;

}
