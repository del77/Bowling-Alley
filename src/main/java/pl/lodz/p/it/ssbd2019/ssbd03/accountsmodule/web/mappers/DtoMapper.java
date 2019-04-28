package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.mappers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto.ComplexAccountDto;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DtoMapper {
    public static List<String> getListOfAccessLevels(ComplexAccountDto editUserDto) {
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
