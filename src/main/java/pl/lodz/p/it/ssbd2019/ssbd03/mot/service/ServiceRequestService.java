package pl.lodz.p.it.ssbd2019.ssbd03.mot.service;

import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.DataAccessException;
import pl.lodz.p.it.ssbd2019.ssbd03.mot.web.dto.ServiceRequestEditDto;
import pl.lodz.p.it.ssbd2019.ssbd03.mot.web.dto.ServiceRequestViewDto;

import java.util.List;

public interface ServiceRequestService {
    /**
     * Tworzy zgłoszenie serwisowe o danych zawartych w podanych w obiekcie.
     *
     * @param alleyId Identyfikator encji toru, dla którego dodajemy zgłoszenie.
     * @param content Treść zgłoszenia.
     * @throws SsbdApplicationException W przypadku wystąpienia błędu dostępu do danych.
     */
    void addServiceRequest(Long alleyId, String content) throws DataAccessException, SsbdApplicationException;

    /**
     * Aktualizuje zgłoszenie serwisowe.
     *
     * @param serviceRequest Obiekt klasy ServiceRequestEditDto, który przechowuje stan po modyfikacji.
     * @throws SsbdApplicationException W przypadku wystąpienia błędu dostępu do danych.
     */
    void updateServiceRequest(ServiceRequestEditDto serviceRequest) throws SsbdApplicationException;

    /**
     * Pobiera wszystkie zgłoszenia serwisowe.
     *
     * @return Lista zgłoszeń serwiswoych w postaci DTO.
     * @throws SsbdApplicationException W przypadku wystąpienia błędu dostępu do danych.
     */
    List<ServiceRequestViewDto> getAllServiceRequests() throws SsbdApplicationException;

    /**
     * Zwraca DTO zgłoszenia serwisowa na bazie jego identyfikatora.
     *
     * @param id Identyfikator encji zgłoszenia serwisowego
     * @return DTO zgłoszenia serwisowego z podanym ID.
     * @throws SsbdApplicationException W przypadk ubłędu dostępu do danych.
     */
    ServiceRequestViewDto getById(Long id) throws SsbdApplicationException;
}
