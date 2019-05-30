package pl.lodz.p.it.ssbd2019.ssbd03.mot.service;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.Score;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.roles.MotRoles;

import javax.annotation.security.DenyAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.util.List;

@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Stateful
@DenyAll
public class ScoreServiceImpl implements ScoreService {
    @Override
    @RolesAllowed(MotRoles.SHOW_USER_SCORE_HISTORY)
    public List<Score> getScoresForUser(Long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    @RolesAllowed(MotRoles.GET_BEST_SCORE_FOR_ALLEY)
    public Score getBestScoreForAlley(Long id) {
        throw new UnsupportedOperationException();
    }
}
