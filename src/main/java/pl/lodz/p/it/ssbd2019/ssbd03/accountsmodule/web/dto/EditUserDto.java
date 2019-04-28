package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.ws.rs.FormParam;

/**
 * Klasa reprezentująca dane z formularza edytcji użytkownika.
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class EditUserDto extends BasicAccountDto{

    @FormParam("clientSelected")
    boolean clientRoleSelected;

    @FormParam("employeeSelected")
    boolean employeeRoleSelected;

    @FormParam("adminSelected")
    boolean adminRoleSelected;
}
