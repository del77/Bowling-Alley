package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.mappers;

import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto.AccessLevelsDto;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class DtoMapper {

    public List<String> getListOfAccessLevels(AccessLevelsDto editUserDto) {
        List<String> selectedAccessLevels = new ArrayList<>();
        if (editUserDto.isClientRoleSelected()) {
            selectedAccessLevels.add("CLIENT");
        }
        if (editUserDto.isEmployeeRoleSelected()) {
            selectedAccessLevels.add("EMPLOYEE");
        }
        if (editUserDto.isAdminRoleSelected()) {
            selectedAccessLevels.add("ADMIN");
        }

        return selectedAccessLevels;
    }
}
