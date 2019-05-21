package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.mappers;

import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto.AccessLevelsSelection;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.roles.AppRoles;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class DtoMapper {

    public List<String> getListOfAccessLevels(AccessLevelsSelection editUserDto) {
        List<String> selectedAccessLevels = new ArrayList<>();
        if (editUserDto.isClientRoleSelected()) {
            selectedAccessLevels.add(AppRoles.CLIENT);
        }
        if (editUserDto.isEmployeeRoleSelected()) {
            selectedAccessLevels.add(AppRoles.EMPLOYEE);
        }
        if (editUserDto.isAdminRoleSelected()) {
            selectedAccessLevels.add(AppRoles.ADMIN);
        }

        return selectedAccessLevels;
    }
}
