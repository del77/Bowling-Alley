package pl.lodz.p.it.ssbd2019.ssbd03.mok.web.rolesretriever;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.AccountAccessLevel;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.UserAccount;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.roles.AppRoles;

import javax.mvc.Models;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserRolesRetriever {
    public static void putAccessLevelsIntoModel(UserAccount userAccount, Models models) {

        for (AccountAccessLevel accountAccessLevel : userAccount.getAccountAccessLevels()) {
            if (accountAccessLevel.isActive()) {
                switch (accountAccessLevel.getAccessLevel().getName()) {
                    case AppRoles.CLIENT:
                        models.put("clientActive", true);
                        break;
                    case AppRoles.EMPLOYEE:
                        models.put("employeeActive", true);
                        break;
                    case AppRoles.ADMIN:
                        models.put("adminActive", true);
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
