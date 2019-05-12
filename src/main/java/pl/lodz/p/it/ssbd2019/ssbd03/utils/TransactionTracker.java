package pl.lodz.p.it.ssbd2019.ssbd03.utils;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.SessionSynchronization;
import java.util.logging.Logger;

public class TransactionTracker implements SessionSynchronization {

    private static final Logger logger = Logger.getLogger(TransactionTracker.class.getName());

    private long txId;

    @Resource
    private SessionContext sessionContext;

    @Override
    public void afterBegin() {
        txId = System.currentTimeMillis();
        String user = getUserName();
        String msg = String.format("Transakcja o ID: %d, dla uzytkownika: %s, zostala rozpoczeta.", txId, user);
        logger.info(msg);
    }

    @Override
    public void beforeCompletion() {
        String user = getUserName();
        String msg = String.format("Transakcja o ID: %d, dla uzytkownika: %s, przed zakończeniem.", txId, user);
        logger.info(msg);
    }

    @Override
    public void afterCompletion(boolean committed) {
        String user = getUserName();
        String msg = String.format("Transakcja o ID: %d, dla uzytkownika: %s, zostala zakonczona przez: %s.", txId, user, (committed?"zatwierdzenie":"wycofanie"));
        logger.info(msg);
    }

    private String getUserName() {
        return sessionContext.getCallerPrincipal().getName();
    }

}
