package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.mappers;

import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto.EditUserDto;

import java.util.ArrayList;
import java.util.List;

public class EditUserDtoMapper {
    public static List<String> editUserDtoToListOfAccessLevels(EditUserDto editUserDto) {
        List<String> selectedAccessLevels = new ArrayList<>();
        if(editUserDto.isClientRoleSelected()) { selectedAccessLevels.add("CLIENT"); }
        if(editUserDto.isEmployeeRoleSelected()) { selectedAccessLevels.add("EMPLOYEE"); }
        if(editUserDto.isAdminRoleSelected()) { selectedAccessLevels.add("ADMIN"); }

        return selectedAccessLevels;
    }
}
