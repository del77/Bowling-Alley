package pl.lodz.p.it.ssbd2019.ssbd03.mot.repository;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.ReservationItem;
import pl.lodz.p.it.ssbd2019.ssbd03.repository.CruRepository;

import javax.ejb.Local;

@Local
public interface ReservationRepositoryLocal extends CruRepository<ReservationItem, Integer> {
}
