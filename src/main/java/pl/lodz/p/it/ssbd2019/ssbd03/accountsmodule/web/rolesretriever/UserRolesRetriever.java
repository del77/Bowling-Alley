package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.rolesretriever;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.AccountAccessLevel;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.UserAccount;

import javax.enterprise.context.ApplicationScoped;
import javax.mvc.Models;

@ApplicationScoped
public class UserRolesRetriever {
    public void putAccessLevelsIntoModel(UserAccount userAccount, Models models) {
        for (AccountAccessLevel accountAccessLevel : userAccount.getAccountAccessLevels()) {
            if (accountAccessLevel.isActive()) {
                switch (accountAccessLevel.getAccessLevel().getName()) {
                    case "CLIENT":
                        models.put("clientActive", true);
                        break;
                    case "EMPLOYEE":
                        models.put("employeeActive", true);
                        break;
                    case "ADMIN":
                        models.put("adminActive", true);
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
