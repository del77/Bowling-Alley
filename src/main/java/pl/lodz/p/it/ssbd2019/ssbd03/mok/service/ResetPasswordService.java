package pl.lodz.p.it.ssbd2019.ssbd03.mok.service;

import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;

public interface ResetPasswordService {
    /**
     * Metoda obsługująca żądanie resetowania hasła przez użytkownika.
     *
     * @param email Adres email powiązany z kontem
     * @throws SsbdApplicationException gdy nie uda się obsłużyć żądania
     */
    void requestResetPassword(String email) throws SsbdApplicationException;

    /**
     * Metoda obsługująca resetowanie hasła przez użytkownika.
     *
     * @param token       Unikalny token pozwalający zmienić hasło
     * @param newPassword Nowe hasło użytkownika
     * @throws SsbdApplicationException gdy nie uda się zmienić hasła
     */
    void resetPassword(String token, String newPassword) throws SsbdApplicationException;
}
