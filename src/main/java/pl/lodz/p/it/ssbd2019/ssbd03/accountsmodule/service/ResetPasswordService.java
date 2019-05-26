package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.service;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.ResetPasswordToken;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.UserAccount;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.generalized.ResetPasswordException;

public interface ResetPasswordService {
    /**
     * Metoda obsługująca żądanie resetowania hasła przez użytkownika.
     *
     * @param email Adres email powiązany z kontem
     * @throws SsbdApplicationException gdy nie uda się obsłużyć żądania
     */
    ResetPasswordToken requestResetPassword(String email) throws SsbdApplicationException;

    /**
     * Metoda obsługująca resetowanie hasła przez użytkownika.
     *
     * @param token       Unikalny token pozwalający zmienić hasło
     * @param newPassword Nowe hasło użytkownika
     * @throws SsbdApplicationException gdy nie uda się zmienić hasła
     */
    UserAccount resetPassword(String token, String newPassword) throws SsbdApplicationException;
}
