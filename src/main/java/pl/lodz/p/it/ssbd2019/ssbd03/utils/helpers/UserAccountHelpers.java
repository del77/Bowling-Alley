package pl.lodz.p.it.ssbd2019.ssbd03.utils.helpers;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.AccountAccessLevel;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.UserAccount;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.roles.AppRoles;

public class UserAccountHelpers {
    private UserAccountHelpers() {}

    /**
     * Metoda pomocnicza sprawdzająca czy konto użytkownika zostało zautoryzowane
     * @param userAccount obiekt konta uzytkownika
     * @return true jeśli konto zostało zautoryzowane, false w przeciwnym wypadku
     */
    public static boolean isUserAccountConfirmed(UserAccount userAccount) {
        AccountAccessLevel unconfirmedAccountAccessLevel = userAccount.getAccountAccessLevels().stream()
                .filter(x -> x.getAccessLevel().getName().equals(AppRoles.UNCONFIRMED))
                .findFirst()
                .orElse(null);

        return unconfirmedAccountAccessLevel == null || !unconfirmedAccountAccessLevel.isActive();
    }
}