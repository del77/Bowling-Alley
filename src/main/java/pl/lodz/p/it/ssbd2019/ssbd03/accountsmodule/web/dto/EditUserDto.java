package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto;

import lombok.Data;
import javax.ws.rs.FormParam;

@Data
public class EditUserDto extends UserAccountDto{
    @FormParam("id")
    Long id;

    @FormParam("clientRole")
    boolean clientRole;

    @FormParam("employeeRole")
    boolean employeeRole;

    @FormParam("adminRole")
    boolean adminRole;

    @FormParam("version")
    long version;
}
