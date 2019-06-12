package pl.lodz.p.it.ssbd2019.ssbd03.mot.service;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.Score;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;
import pl.lodz.p.it.ssbd2019.ssbd03.mot.web.dto.ScoreDto;

import java.util.List;

public interface ScoreService {
    /**
     * Pobiera wyniki osiągnięte przez użytkownika
     *
     * @param id identyfikator użytkownika
     * @return lista wyników użytkownika
     */
    List<Score> getScoresForUser(Long id);

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
    void addNewScore(Long reservation_id, ScoreDto score) throws SsbdApplicationException;
}
