package pl.lodz.p.it.ssbd2019.ssbd03.mot.service;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.Alley;

import java.util.List;

public interface AlleyService {
    /**
     * Wyświetla listę wszystkich torów
     *
     * return Lista obiektów encji reprezentująca tory
     */
    List<Alley> getAllAlleys();

    /**
     * Tworzy tor o danych zawartych w podanym w obiekcie.
     *
     * @param alley Obiekt klasy Alley, który ma zostać dodany do bazy danych.
     */
    void addAlley(Alley alley);

    /**
     * Zmienia flagę zablokowania toru z podanym id
     *
     * @param id identyfikator toru
     * @param isActive nowa wartość flagi zablokowania
     */
    void updateLockStatusOnAlleyById(Long id, boolean isActive);

}
