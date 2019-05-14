package pl.lodz.p.it.ssbd2019.ssbd03.mor.repository;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.Reservation;
import pl.lodz.p.it.ssbd2019.ssbd03.repository.AbstractCruRepository;

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
    @RolesAllowed({"CreateReservation", "CreateReservationForUser"})
    public Reservation create(Reservation reservation) {
        return super.create(reservation);
    }

    @Override
    @RolesAllowed({"EditOwnReservation", "EditReservationForUser", "CancelOwnReservation", "CancelReservationForUser",
            "AddCommentForReservation"})
    public Reservation edit(Reservation reservation) {
        return super.edit(reservation);
    }

    @Override
    @RolesAllowed("GetReservationDetails")
    public Optional<Reservation> findById(Long id) {
        return super.findById(id);
    }
}
