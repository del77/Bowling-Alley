package pl.lodz.p.it.ssbd2019.ssbd03.mor.repository;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.Alley;
import pl.lodz.p.it.ssbd2019.ssbd03.repository.AbstractCruRepository;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.roles.MorRoles;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Stateless
public class AlleyRepositoryImpl extends AbstractCruRepository<Alley, Long> implements AlleyRepositoryLocal {

    @PersistenceContext(unitName = "ssbd03motPU")
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
    @RolesAllowed(MorRoles.GET_RESERVATIONS_FOR_ALLEY)
    public Optional<Alley> findById(Long id) {
        return super.findById(id);
    }
}