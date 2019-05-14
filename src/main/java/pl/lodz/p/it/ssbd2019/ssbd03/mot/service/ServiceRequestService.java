package pl.lodz.p.it.ssbd2019.ssbd03.mot.service;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.ServiceRequest;

import java.util.List;

public interface ServiceRequestService {
    /**
     * Tworzy zgłoszenie serwisowe o danych zawartych w podanych w obiekcie.
     *
     * @param serviceRequest Obiekt klasy ServiceRequest, który ma zostać dodany do bazy danych.
     */
    void addServiceRequest(ServiceRequest serviceRequest);

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
