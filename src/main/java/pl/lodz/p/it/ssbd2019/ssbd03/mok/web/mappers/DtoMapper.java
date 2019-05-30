package pl.lodz.p.it.ssbd2019.ssbd03.mok.web.mappers;

import pl.lodz.p.it.ssbd2019.ssbd03.mok.web.dto.AccessLevelsSelection;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.roles.AppRolesProvider;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class DtoMapper {
    @Inject
    private AppRolesProvider appRolesProvider;

    public List<String> getListOfAccessLevels(AccessLevelsSelection editUserDto) {
        List<String> selectedAccessLevels = new ArrayList<>();
        if (editUserDto.isClientRoleSelected()) {
            selectedAccessLevels.add(appRolesProvider.getClient());
        }
        if (editUserDto.isEmployeeRoleSelected()) {
            selectedAccessLevels.add(appRolesProvider.getEmployee());
        }
        if (editUserDto.isAdminRoleSelected()) {
            selectedAccessLevels.add(appRolesProvider.getAdmin());
        }

        return selectedAccessLevels;
    }
}
