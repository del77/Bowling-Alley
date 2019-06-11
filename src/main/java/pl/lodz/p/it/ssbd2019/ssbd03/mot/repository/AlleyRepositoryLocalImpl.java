package pl.lodz.p.it.ssbd2019.ssbd03.mot.repository;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.Alley;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.DataAccessException;
import pl.lodz.p.it.ssbd2019.ssbd03.repository.AbstractCruRepository;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.roles.MotRoles;

import javax.annotation.security.DenyAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Stateless(name = "MOTAlleyRepository")
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@DenyAll
public class AlleyRepositoryLocalImpl extends AbstractCruRepository<Alley, Long> implements AlleyRepositoryLocal {

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
    @RolesAllowed(MotRoles.GET_ALLEYS_LIST)
    public List<Alley> findAll() throws DataAccessException {
        return super.findAll();
    }

    @Override
    @RolesAllowed(MotRoles.ENABLE_DISABLE_ALLEY)
    public void edit(Alley alley) throws DataAccessException {
        super.edit(alley);
    }

    @Override
    @RolesAllowed({MotRoles.GET_ALLEY_GAMES_HISTORY, MotRoles.GET_BEST_SCORE_FOR_ALLEY})
    public Optional<Alley> findById(Long id) throws DataAccessException {
        return super.findById(id);
    }

    @Override
    @RolesAllowed(MotRoles.ADD_ALLEY)
    public Alley create(Alley alley) throws DataAccessException {
        return super.create(alley);
    }
}
