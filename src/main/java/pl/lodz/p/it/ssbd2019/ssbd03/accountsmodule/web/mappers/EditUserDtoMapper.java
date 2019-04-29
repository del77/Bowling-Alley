package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.mappers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto.AccountDetailsDto;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EditUserDtoMapper {
    public static List<String> editUserDtoToListOfAccessLevels(AccountDetailsDto accountDetailsDto) {
        List<String> selectedAccessLevels = new ArrayList<>();
        if(accountDetailsDto.isClientRoleSelected()) { selectedAccessLevels.add("CLIENT"); }
        if(accountDetailsDto.isEmployeeRoleSelected()) { selectedAccessLevels.add("EMPLOYEE"); }
        if(accountDetailsDto.isAdminRoleSelected()) { selectedAccessLevels.add("ADMIN"); }

        return selectedAccessLevels;
    }
}
