package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.service;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.User;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.EntityRetrievelException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.RegistrationProcessException;

public interface RegistrationService {

    /**
     * Wiąże encje oraz tworzy konto użytkownika o danych zawartych w podanych obiektach.
     * @param account Obiekt klasy Account, który ma zostać dodany do bazy danych.
     * @param user Obiekt klasy user, który ma zostać dodany do bazy danych.
     * @throws RegistrationProcessException W przypadku niespodziewanego błędu w trakcie rejestracji.
     * @throws EntityRetrievelException W przypadku, gdy nie jest w stanie pozyskać encji AccessLevel.
     */
    void registerAccount(Account account, User user) throws RegistrationProcessException, EntityRetrievelException;

    /**
     * Potwierdzenie konta użytkownika.
     * Metoda powstała na potrzeby weryfikacji po aktywacji po e-mail.
     * @param accountId Identyfikator konta
     */
    void confirmAccount(long accountId) throws EntityRetrievelException;
}
