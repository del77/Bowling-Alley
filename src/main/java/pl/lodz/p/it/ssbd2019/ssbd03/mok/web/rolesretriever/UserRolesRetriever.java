package pl.lodz.p.it.ssbd2019.ssbd03.mok.web.rolesretriever;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.AccountAccessLevel;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.UserAccount;
import pl.lodz.p.it.ssbd2019.ssbd03.mok.web.dto.AccountDetailsDto;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.roles.AppRoles;

import javax.mvc.Models;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserRolesRetriever {
    public static void putAccessLevelsIntoModel(AccountDetailsDto userAccount, Models models) {


        models.put("clientActive", true);

        models.put("employeeActive", true);

        models.put("adminActive", true);


    }
}


