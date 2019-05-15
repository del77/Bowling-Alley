package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.service;

import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.ResetPasswordException;

public interface ResetPasswordService {
    /**
     * Metoda obsługująca żądanie resetowania hasła przez użytkownika.
     *
     * @param email Adres email powiązany z kontem
     * @throws ResetPasswordException Wyjątek
     */
    void requestResetPassword(String email) throws ResetPasswordException;

    /**
     * Metoda obsługująca resetowanie hasła przez użytkownika.
     *
     * @param token    Unikalny token pozwalający zmienić hasło
     * @param newPassword Nowe hasło użytkownika
     * @throws ResetPasswordException Wyjątek
     */
    void resetPassword(String token, String newPassword) throws ResetPasswordException;
}
