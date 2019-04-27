package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto;

import lombok.Data;
import javax.ws.rs.FormParam;

/**
 * Klasa reprezentująca dane z formularza edytcji użytkownika.
 */
@Data
public class EditUserDto extends BasicAccountDto{
    @FormParam("id")
    Long id;

    @FormParam("clientVersion")
    Long clientRoleVersion;

    @FormParam("clientSelected")
    boolean clientRoleSelected;



    @FormParam("employeeVersion")
    Long employeeRoleVersion;

    @FormParam("employeeSelected")
    boolean employeeRoleSelected;



    @FormParam("adminVersion")
    Long adminRoleVersion;

    @FormParam("adminSelected")
    boolean adminRoleSelected;


    @FormParam("version")
    long version;
}
