package pl.lodz.p.it.ssbd2019.ssbd03.mok.service;

import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;
import pl.lodz.p.it.ssbd2019.ssbd03.mok.web.dto.BasicAccountDto;

import java.util.List;

public interface RegistrationService {

    /**
     * Wiąże encje oraz tworzy konto użytkownika o danych zawartych w podanych obiektach.
     *
     * @param userAccount      Obiekt klasy UserAccount, który ma zostać dodany do bazy danych.
     * @param accessLevelNames Lista obiektów klasy String, który definiuje poziom dostępu.
     * @throws SsbdApplicationException W przypadku gdy nie uda się stworzyć konta użytkownika.
     */

    void registerAccount(BasicAccountDto userAccount, List<String> accessLevelNames)
            throws SsbdApplicationException;

}
