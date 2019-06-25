package pl.lodz.p.it.ssbd2019.ssbd03.mor.repository;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.Alley;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.DataAccessException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.EntityRetrievalException;
import pl.lodz.p.it.ssbd2019.ssbd03.repository.AbstractCruRepository;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.roles.MorRoles;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Stateless(name = "MORAlleyRepository")
public class AlleyRepositoryLocalImpl extends AbstractCruRepository<Alley, Long> implements AlleyRepositoryLocal {

    @PersistenceContext(unitName = "ssbd03morPU")
    private EntityManager entityManager;

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    protected Class<Alley> getTypeParameterClass() {
        return Alley.class;
    }

    @Override
    @RolesAllowed({MorRoles.GET_RESERVATIONS_FOR_ALLEY, MorRoles.CREATE_RESERVATION})
    public Optional<Alley> findById(Long id) throws DataAccessException {
        return super.findById(id);
    }
    
    @Override
    @RolesAllowed({MorRoles.CREATE_RESERVATION, MorRoles.CREATE_RESERVATION_FOR_USER})
    public List<Alley> getAvailableAlleysInTimeRange(Timestamp startTime, Timestamp endTime) throws DataAccessException {
        try {
            TypedQuery<Alley> namedQuery = this.createNamedQuery("Alley.findAlleysNotReservedBetweenTimes");
            namedQuery.setParameter("startTime", startTime);
            namedQuery.setParameter("endTime", endTime);
            return namedQuery.getResultList();
        } catch (Exception e) {
            throw new EntityRetrievalException(e.getMessage());
        }
    }
    
    @Override
    @RolesAllowed({MorRoles.CREATE_RESERVATION, MorRoles.CREATE_RESERVATION_FOR_USER})
    public List<Alley> getAvailableAlleysInTimeRangeExcludingReservation(Timestamp startTime, Timestamp endTime, Long reservationId) throws DataAccessException {
        try {
            TypedQuery<Alley> namedQuery = this.createNamedQuery("Alley.findAlleysNotReservedBetweenTimesExcludingReservation");
            namedQuery.setParameter("startTime", startTime);
            namedQuery.setParameter("endTime", endTime);
            namedQuery.setParameter("excludedReservationId", reservationId);
            return namedQuery.getResultList();
        } catch (Exception e) {
            throw new EntityRetrievalException(e.getMessage());
        }
    }
    
    @Override
    @PermitAll
    public Optional<Alley> findByNumber(int number) throws DataAccessException {
        try {
            TypedQuery<Alley> namedQuery = this.createNamedQuery("Alley.findByNumber");
            namedQuery.setParameter("number", number);
            return Optional.of(namedQuery.getSingleResult());
        } catch (Exception e) {
            throw new EntityRetrievalException(e);
        }
    }
}