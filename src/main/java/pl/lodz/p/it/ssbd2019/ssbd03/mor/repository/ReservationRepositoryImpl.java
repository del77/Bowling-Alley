package pl.lodz.p.it.ssbd2019.ssbd03.mor.repository;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.Reservation;
import pl.lodz.p.it.ssbd2019.ssbd03.repository.AbstractCruRepository;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.roles.MorRoles;

import javax.annotation.security.DenyAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Stateless
@DenyAll
public class ReservationRepositoryImpl extends AbstractCruRepository<Reservation, Long> implements ReservationRepositoryLocal {

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
    public Reservation create(Reservation reservation) {
        return super.create(reservation);
    }

    @Override
    @RolesAllowed({MorRoles.EDIT_OWN_RESERVATION, MorRoles.EDIT_RESERVATION_FOR_USER, MorRoles.CANCEL_OWN_RESERVATION, MorRoles.CANCEL_RESERVATION_FOR_USER,
            MorRoles.ADD_COMMENT_FOR_RESERVATION})
    public Reservation edit(Reservation reservation) {
        return super.edit(reservation);
    }

    @Override
    @RolesAllowed(MorRoles.GET_RESERVATION_DETAILS)
    public Optional<Reservation> findById(Long id) {
        return super.findById(id);
    }
}
