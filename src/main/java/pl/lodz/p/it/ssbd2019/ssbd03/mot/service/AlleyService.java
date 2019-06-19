package pl.lodz.p.it.ssbd2019.ssbd03.mot.service;

import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;
import pl.lodz.p.it.ssbd2019.ssbd03.mot.web.dto.AlleyCreationDto;
import pl.lodz.p.it.ssbd2019.ssbd03.mot.web.dto.AlleyDto;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.roles.MotRoles;

import javax.annotation.security.RolesAllowed;
import java.util.List;

public interface AlleyService {
    /**
     * Wyświetla listę wszystkich torów
     *
     * return Lista obiektów encji reprezentująca tory
     */
    List<AlleyDto> getAllAlleys() throws SsbdApplicationException;

    @RolesAllowed(MotRoles.GET_ALLEYS_LIST)
    AlleyDto getById(Long id) throws SsbdApplicationException;

    /**
     * Tworzy tor o danych zawartych w podanym w obiekcie.
     *
     * @param alleyDto DTO toru, który ma zostać dodany do bazy danych.
     */
    void addAlley(AlleyCreationDto alleyDto) throws SsbdApplicationException;

    /**
     * Zmienia flagę zablokowania toru z podanym id
     *
     * @param id identyfikator toru
     * @param isActive nowa wartość flagi zablokowania
     */
    void updateLockStatusOnAlleyById(Long id, boolean isActive);

}
