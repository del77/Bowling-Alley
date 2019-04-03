package pl.lodz.p.it.ssbd2019.ssbd03.alleysmodule.repository;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.Score;
import pl.lodz.p.it.ssbd2019.ssbd03.repository.CruRepository;

import javax.ejb.Local;

@Local
public interface ScoreRepositoryLocal extends CruRepository<Score, Integer> {
}
