package pl.lodz.p.it.ssbd2019.ssbd03.mot.service;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.Score;
import pl.lodz.p.it.ssbd2019.ssbd03.mot.repository.ScoreRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.mot.web.dto.ScoreDto;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.roles.MotRoles;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.tracker.InterceptorTracker;

import javax.annotation.security.DenyAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import java.util.List;

@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Interceptors(InterceptorTracker.class)
@Stateful
@DenyAll
public class ScoreServiceImpl implements ScoreService {

    @EJB(beanName = "MOTScoreRepository")
    ScoreRepositoryLocal scoreRepositoryLocal;

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

    @Override
    @RolesAllowed(MotRoles.ADD_SCORE)
    public void addNewScore(ScoreDto score) {
        // todo
    }
}
