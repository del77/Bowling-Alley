package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.service;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.UserAccount;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.EntityRetrievalException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.EntityUpdateException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.NotUniqueParameterException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.RegistrationProcessException;

import java.util.List;

public interface RegistrationService {

    /**
     * Wiąże encje oraz tworzy konto użytkownika o danych zawartych w podanych obiektach.
     *
     * @param userAccount      Obiekt klasy UserAccount, który ma zostać dodany do bazy danych.
     * @param accessLevelNames Lista obiektów klasy String, który definiuje poziom dostępu.
     * @throws RegistrationProcessException W przypadku niespodziewanego błędu w trakcie rejestracji.
     * @throws EntityRetrievalException     W przypadku, gdy nie jest w stanie pozyskać encji AccessLevel.
     * @throws NotUniqueParameterException  W przypadku, gdy login lub email nie jest unikalny.
     */

    void registerAccount(UserAccount userAccount, List<String> accessLevelNames) throws RegistrationProcessException, EntityRetrievalException, NotUniqueParameterException;

    /**
     * Potwierdzenie konta użytkownika.
     * Metoda powstała na potrzeby weryfikacji po aktywacji po e-mail.
     *
     * @param accountId Identyfikator konta
     */
    void confirmAccount(long accountId) throws EntityRetrievalException, EntityUpdateException;
}
