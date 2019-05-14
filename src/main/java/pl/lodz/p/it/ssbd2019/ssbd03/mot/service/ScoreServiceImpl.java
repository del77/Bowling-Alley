package pl.lodz.p.it.ssbd2019.ssbd03.mot.service;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.Score;

import javax.annotation.security.RolesAllowed;
import java.util.List;

public class ScoreServiceImpl implements ScoreService {
    @Override
    @RolesAllowed("ShowUserScoreHistory")
    public List<Score> getScoresForUser(Long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    @RolesAllowed("GetBestScoreForAlley")
    public Score getBestScoreForAlley(Long id) {
        throw new UnsupportedOperationException();
    }
}
