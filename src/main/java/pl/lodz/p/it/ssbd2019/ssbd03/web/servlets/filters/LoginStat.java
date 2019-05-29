package pl.lodz.p.it.ssbd2019.ssbd03.web.servlets.filters;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.UserAccount;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.DataAccessException;
import pl.lodz.p.it.ssbd2019.ssbd03.mok.repository.UserAccountRepositoryLocal;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.mvc.Models;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.logging.Logger;

@SessionScoped
class LoginStat implements Serializable {

    private static final Logger logger = Logger.getLogger(LoginStat.class.getName());
    private static final String DATE_PATTERN = "MM/dd/yyyy HH:mm:ss";

    @EJB(beanName = "MOKUserRepository")
    private UserAccountRepositoryLocal userAccountRepositoryLocal;

    private transient UserAccount userAccount;

    void tryFillModelsWithLoginStat(String userName, Models models) {
        try {
            initializeIfNeeded(userName);
            fillModelsWithLoginStat(models);
        } catch (DataAccessException e) {
            logger.severe(e.getLocalizedMessage());
        }
    }

    private void initializeIfNeeded(String userName) throws DataAccessException {
        if (userAccount == null) {
            userAccount = userAccountRepositoryLocal
                    .findByLogin(userName)
                    .orElseThrow(() -> new DataAccessException("Could not retrieve user."));
        }
    }

    private void fillModelsWithLoginStat(Models models) {
        String successfulLoginDate = retrieveSuccessfulLoginDate(userAccount);
        String failedLoginDate = retrieveFailedLoginDate(userAccount);
        models.put("successfulLoginDate", successfulLoginDate);
        models.put("failedLoginDate", failedLoginDate);
    }

    private String retrieveFailedLoginDate(UserAccount userAccount) {
        Timestamp timestamp = userAccount.getLastFailedLogin();
        return formatTimestamp(timestamp);
    }

    private String retrieveSuccessfulLoginDate(UserAccount userAccount) {
        Timestamp timestamp = userAccount.getLastSuccessfulLogin();
        return formatTimestamp(timestamp);
    }

    private String formatTimestamp(Timestamp timestamp) {
        if (timestamp != null) {
            return new SimpleDateFormat(DATE_PATTERN).format(timestamp);
        } else {
            return "--|--";
        }
    }
}
