package pl.lodz.p.it.ssbd2019.ssbd03.mot.service;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.Score;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;
import pl.lodz.p.it.ssbd2019.ssbd03.mot.web.dto.AddScoreDto;
import pl.lodz.p.it.ssbd2019.ssbd03.mot.web.dto.ScoreDto;

import java.util.List;

public interface ScoreService {
    /**
     * Pobiera wyniki osiągnięte przez użytkownika
     *
     * @param id identyfikator użytkownika
     * @throws SsbdApplicationException gdy nie uda sie pobrać wyników użytkownika
     * @return lista wyników użytkownika
     */
    List<ScoreDto> getScoresForUser(Long id) throws SsbdApplicationException;

    /**
     * Wyświetla najlepszy wynik osiągnięty na wybranym torze
     *
     * @param id identyfikator toru
     * @return Obiekt encji reprezentujący najlepszy wynik dla toru
     */
    Score getBestScoreForAlley(Long id);


    /**
     * Dodaje nowy wynik i aktualizuje najwyższy wynik na torze
     *
     * @param reservation_id ID rezerwacji do której dopisany jest wynik
     * @param score          DTO zawierające wynik oraz login użytkownika
     * @throws SsbdApplicationException wyjątek rzucany w prztpadku błędu przy dodawaniu wyniku
     */
    void addNewScore(Long reservation_id, AddScoreDto score) throws SsbdApplicationException;
}
