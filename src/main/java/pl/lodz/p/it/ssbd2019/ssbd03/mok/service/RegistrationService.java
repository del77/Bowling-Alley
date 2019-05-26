package pl.lodz.p.it.ssbd2019.ssbd03.mok.service;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.UserAccount;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.EntityRetrievalException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.NotUniqueEmailException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.NotUniqueLoginException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.generalized.RegistrationProcessException;

import java.util.List;

public interface RegistrationService {

    /**
     * Wiąże encje oraz tworzy konto użytkownika o danych zawartych w podanych obiektach.
     *
     * @param userAccount      Obiekt klasy UserAccount, który ma zostać dodany do bazy danych.
     * @param accessLevelNames Lista obiektów klasy String, który definiuje poziom dostępu.
     * @throws SsbdApplicationException W przypadku gdy nie uda się stworzyć konta użytkownika.
     */

    void registerAccount(UserAccount userAccount, List<String> accessLevelNames)
            throws SsbdApplicationException;

    /**
     * Potwierdzenie konta użytkownika.
     * Metoda powstała na potrzeby weryfikacji po aktywacji po e-mail.
     *
     * @param accountId Identyfikator konta
     */
    void confirmAccount(long accountId) throws SsbdApplicationException;
}
