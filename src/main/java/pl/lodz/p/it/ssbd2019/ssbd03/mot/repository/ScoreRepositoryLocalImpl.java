package pl.lodz.p.it.ssbd2019.ssbd03.mot.repository;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.Score;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;
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
import java.util.List;

@Stateless(name = "MOTScoreRepository")
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@DenyAll
public class ScoreRepositoryLocalImpl extends AbstractCruRepository<Score, Long> implements ScoreRepositoryLocal {

    @PersistenceContext(unitName = "ssbd03motPU")
    private EntityManager entityManager;

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    protected Class<Score> getTypeParameterClass() {
        return Score.class;
    }

    @Override
    @RolesAllowed({MotRoles.GET_ALLEY_GAMES_HISTORY})
    public List<Score> getScoresByReservation(Long id) throws SsbdApplicationException {
        try {
            TypedQuery<Score> namedQuery = this.createNamedQuery("Score.findScoresForReservation");
            namedQuery.setParameter("reservationId", id);
            return namedQuery.getResultList();
        } catch (Exception e) {
            throw new EntityRetrievalException("Couldn't retrieve scores for given reservation", e);
        }

    }
}
