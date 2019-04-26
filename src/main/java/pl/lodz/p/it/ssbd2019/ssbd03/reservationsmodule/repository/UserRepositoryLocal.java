package pl.lodz.p.it.ssbd2019.ssbd03.reservationsmodule.repository;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.UserAccount;
import pl.lodz.p.it.ssbd2019.ssbd03.repository.CruRepository;

import javax.ejb.Local;

@Local
public interface UserRepositoryLocal extends CruRepository<UserAccount, Integer> {
}
