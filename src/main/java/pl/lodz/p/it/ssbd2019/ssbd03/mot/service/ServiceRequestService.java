package pl.lodz.p.it.ssbd2019.ssbd03.mot.service;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.ServiceRequest;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.DataAccessException;

import java.util.List;

public interface ServiceRequestService {
    /**
     * Tworzy zgłoszenie serwisowe o danych zawartych w podanych w obiekcie.
     *
     * @param alleyId Id encji toru, dla którego dodajemy zgłoszenie.
     * @param content Treść zgłoszenia.
     */
    void addServiceRequest(Long alleyId, String content) throws DataAccessException, SsbdApplicationException;

    /**
     * Aktualizuje zgłoszenie serwisowe
     *
     * @param serviceRequest Obiekt klasy ServiceRequest, który przechowuje stan po modyfikacji.
     */
    void updateServiceRequest(ServiceRequest serviceRequest);

    /**
     * Pobiera wszystkie zgłoszenia serwisowe
     *
     * @return Lista zgłoszeń serwiswoych
     */
    List<ServiceRequest> getAllServiceRequests();
}
