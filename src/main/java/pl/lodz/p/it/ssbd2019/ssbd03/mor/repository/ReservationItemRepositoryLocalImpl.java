package pl.lodz.p.it.ssbd2019.ssbd03.mor.repository;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.ReservationItem;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.ReservationItemId;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.*;
import pl.lodz.p.it.ssbd2019.ssbd03.repository.AbstractCruRepository;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.roles.MorRoles;

import javax.annotation.security.DenyAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.*;
import javax.validation.ConstraintViolationException;
import java.util.List;

@Stateless(name = "MORReservationItemRepository")
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@DenyAll
public class ReservationItemRepositoryLocalImpl extends AbstractCruRepository<ReservationItem, ReservationItemId> implements ReservationItemRepositoryLocal  {
    
    @PersistenceContext(unitName = "ssbd03morPU")
    private EntityManager entityManager;
    
    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }
    
    @Override
    protected Class<ReservationItem> getTypeParameterClass() {
        return ReservationItem.class;
    }
    
    @Override
    @RolesAllowed({MorRoles.EDIT_RESERVATION_FOR_USER, MorRoles.EDIT_OWN_RESERVATION,
            MorRoles.CREATE_RESERVATION_FOR_USER, MorRoles.CREATE_RESERVATION})
    public ReservationItem create(ReservationItem reservationItem) throws DataAccessException {
        return super.create(reservationItem);
    }
    
    @Override
    @RolesAllowed({MorRoles.EDIT_OWN_RESERVATION, MorRoles.EDIT_RESERVATION_FOR_USER, MorRoles.CANCEL_OWN_RESERVATION, MorRoles.CANCEL_RESERVATION_FOR_USER,
            MorRoles.ADD_COMMENT_FOR_RESERVATION, MorRoles.DISABLE_COMMENT})
    public void edit(ReservationItem reservationItem) throws DataAccessException {
        try {
            super.edit(reservationItem);
        } catch (OptimisticLockException e) {
            throw new ReservationItemOptimisticLockException(e);
        } catch (ConstraintViolationException e) {
            throw new DatabaseConstraintViolationException(e);
        } catch (PersistenceException e) {
            throw new EntityUpdateException(e);
        }
    }
    
    @Override
    @RolesAllowed({MorRoles.EDIT_RESERVATION_FOR_USER, MorRoles.EDIT_OWN_RESERVATION,
            MorRoles.GET_OWN_RESERVATION_DETAILS, MorRoles.GET_RESERVATION_DETAILS})
    public List<ReservationItem> getItemsForReservation(Long reservationId) throws DataAccessException {
        try {
            TypedQuery<ReservationItem> namedQuery = this.createNamedQuery("ReservationItem.getItemsForReservation");
            namedQuery.setParameter("reservationId", reservationId);
            return namedQuery.getResultList();
        } catch (Exception e) {
            throw new EntityRetrievalException(e);
        }
    }
}
