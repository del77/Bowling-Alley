package pl.lodz.p.it.ssbd2019.ssbd03.utils.helpers;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.AccountAccessLevel;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.UserAccount;

public class UserAccountHelpers {
    private UserAccountHelpers() {}

    /**
     * Metoda pomocnicza sprawdzająca czy konto użytkownika posiada daną rolę
     * @param userAccount obiekt konta uzytkownika
     * @param role sprawdzana rola
     * @return true jeśli konto zostało zautoryzowane, false w przeciwnym wypadku
     */
    public static boolean isUserAccountConfirmed(UserAccount userAccount, String role) {
        AccountAccessLevel unconfirmedAccountAccessLevel = userAccount.getAccountAccessLevels().stream()
                .filter(x -> x.getAccessLevel().getName().equals(role))
                .findFirst()
                .orElse(null);

        return unconfirmedAccountAccessLevel == null || !unconfirmedAccountAccessLevel.isActive();
    }
}