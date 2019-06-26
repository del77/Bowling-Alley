package pl.lodz.p.it.ssbd2019.ssbd03.mot.repository;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.Reservation;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.DataAccessException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.EntityRetrievalException;
import pl.lodz.p.it.ssbd2019.ssbd03.repository.AbstractCruRepository;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.roles.MotRoles;

import javax.annotation.security.DenyAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Stateless(name = "MOTReservationRepository")
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@DenyAll
public class ReservationRepositoryLocalImpl extends AbstractCruRepository<Reservation, Long> implements ReservationRepositoryLocal {

    @PersistenceContext(unitName = "ssbd03motPU")
    private EntityManager entityManager;

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    protected Class<Reservation> getTypeParameterClass() {
        return Reservation.class;
    }

    @Override
    @RolesAllowed(MotRoles.ENTER_GAME_RESULT)
    public void edit(Reservation reservation) throws DataAccessException {
        super.edit(reservation);
    }

    @Override
    @RolesAllowed({MotRoles.GET_ALLEY_GAMES_HISTORY})
    public List<Reservation> findFinishedReservationsForAlley(Long alleyId) throws SsbdApplicationException {
        try {
            TypedQuery<Reservation> namedQuery = this.createNamedQuery("Reservation.findReservationsFinishedBeforeDateForAlley");
            namedQuery.setParameter("alleyId", alleyId);
            namedQuery.setParameter("endDate", new Timestamp(System.currentTimeMillis()));
            return namedQuery.getResultList();
        } catch (Exception e) {
            throw new EntityRetrievalException("Couldnt retrieve reservations for given alley", e);
        }
    }
}
