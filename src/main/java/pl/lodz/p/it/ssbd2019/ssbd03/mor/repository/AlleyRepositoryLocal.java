package pl.lodz.p.it.ssbd2019.ssbd03.mor.repository;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.Alley;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.DataAccessException;
import pl.lodz.p.it.ssbd2019.ssbd03.repository.CruRepository;

import javax.ejb.Local;
import java.sql.Timestamp;
import java.util.List;

@Local
public interface AlleyRepositoryLocal extends CruRepository<Alley, Long> {

    List<Alley> getAvailableAlleysForTimeRange(Timestamp startTime, Timestamp endTime) throws DataAccessException;
}