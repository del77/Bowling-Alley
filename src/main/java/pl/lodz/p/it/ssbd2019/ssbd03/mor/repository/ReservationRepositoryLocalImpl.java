package pl.lodz.p.it.ssbd2019.ssbd03.mor.repository;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.Reservation;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.DataAccessException;
import pl.lodz.p.it.ssbd2019.ssbd03.repository.AbstractCruRepository;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.roles.MorRoles;

import javax.annotation.security.DenyAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionRolledbackLocalException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Stateless(name = "MORReservationRepository")
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@DenyAll
public class ReservationRepositoryLocalImpl extends AbstractCruRepository<Reservation, Long> implements ReservationRepositoryLocal {

    @PersistenceContext(unitName = "ssbd03morPU")
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
    @RolesAllowed({MorRoles.CREATE_RESERVATION, MorRoles.CREATE_RESERVATION_FOR_USER})
    public Reservation create(Reservation reservation) throws DataAccessException {
        return super.create(reservation);
    }

    @Override
    @RolesAllowed({MorRoles.EDIT_OWN_RESERVATION, MorRoles.EDIT_RESERVATION_FOR_USER, MorRoles.CANCEL_OWN_RESERVATION, MorRoles.CANCEL_RESERVATION_FOR_USER,
            MorRoles.ADD_COMMENT_FOR_RESERVATION})
    public void edit(Reservation reservation) throws DataAccessException {
        super.edit(reservation);
    }

    @Override
    @RolesAllowed({MorRoles.GET_RESERVATION_DETAILS, MorRoles.EDIT_RESERVATION_FOR_USER})
    public Optional<Reservation> findById(Long id) throws DataAccessException {
        return super.findById(id);
    }

    @Override
    @RolesAllowed({MorRoles.GET_RESERVATION_DETAILS, MorRoles.EDIT_RESERVATION_FOR_USER, MorRoles.GET_OWN_RESERVATIONS})
    public List<Reservation> findReservationsForUser(Long userId) throws DataAccessException {
        try {
            TypedQuery<Reservation> namedQuery = this.createNamedQuery("Reservation.findReservationsForUser");
            namedQuery.setParameter("userId", userId);
            return namedQuery.getResultList();
        } catch (TransactionRolledbackLocalException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    @RolesAllowed({MorRoles.GET_RESERVATIONS_FOR_ALLEY})
    public List<Reservation> findReservationsForAlley(Long alleyId) throws DataAccessException {
        try {
            TypedQuery<Reservation> namedQuery = this.createNamedQuery("Reservation.findReservationsForAlley");
            namedQuery.setParameter("alleyId", alleyId);
            return namedQuery.getResultList();
        } catch (TransactionRolledbackLocalException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
