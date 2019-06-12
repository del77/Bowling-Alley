package pl.lodz.p.it.ssbd2019.ssbd03.mot.service;

import org.hibernate.Hibernate;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.Alley;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.Reservation;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.Score;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.UserAccount;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.*;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.generalized.AddScoreException;
import pl.lodz.p.it.ssbd2019.ssbd03.mot.repository.AlleyRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.mot.repository.ReservationRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.mot.repository.ScoreRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.mot.repository.UserAccountRepositoryLocal;
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
import javax.validation.ConstraintViolationException;
import java.util.List;

@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Interceptors(InterceptorTracker.class)
@Stateful
@DenyAll
public class ScoreServiceImpl implements ScoreService {

    @EJB(beanName = "MOTScoreRepository")
    ScoreRepositoryLocal scoreRepositoryLocal;

    @EJB(beanName = "MOTReservationRepository")
    ReservationRepositoryLocal reservationRepositoryLocal;

    @EJB(beanName = "MOTUserRepository")
    UserAccountRepositoryLocal userAccountRepositoryLocal;

    @EJB(beanName = "MOTAlleyRepository")
    AlleyRepositoryLocal alleyRepositoryLocal;

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
    public void addNewScore(Long reservationId, ScoreDto scoreDto) throws SsbdApplicationException {
        Reservation reservation = reservationRepositoryLocal.findById(reservationId).orElseThrow(ReservationDoesNotExistException::new);
        UserAccount userAccount = userAccountRepositoryLocal.findByLogin(scoreDto.getLogin()).orElseThrow(LoginDoesNotExistException::new);

        Score score = Score.builder()
                .reservation(reservation)
                .userAccount(userAccount)
                .score(scoreDto.getScore())
                .build();

        try {
            scoreRepositoryLocal.create(score);
            updateMaxScore(reservation.getAlley(), score.getScore());
        } catch (ConstraintViolationException e) {
            throw new ScoreConstraintViolationException();
        } catch (Exception e) {
            throw new AddScoreException();
        }
    }

    /**
     * Aktualizuje najwyższy wynik na torze
     *
     * @param alley tor dla którego aktualizujemy wynik
     * @param score wynik
     * @throws DataAccessException wyjątek rzucany w przypadku błędu dostępu do danych
     */
    private void updateMaxScore(Alley alley, int score) throws DataAccessException {
        if (alley.getMaxScore() < score) {
            alley.setMaxScore(score);
            alleyRepositoryLocal.edit(alley);
        }
    }
}
