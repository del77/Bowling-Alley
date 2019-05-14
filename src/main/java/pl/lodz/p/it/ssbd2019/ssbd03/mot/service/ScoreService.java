package pl.lodz.p.it.ssbd2019.ssbd03.mot.service;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.Score;

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
     * @param id identyfikator toru
     * @return Obiekt encji reprezentujący najlepszy wynik dla toru
     */
    Score getBestScoreForAlley(Long id);
}
