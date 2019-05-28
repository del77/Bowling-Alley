package pl.lodz.p.it.ssbd2019.ssbd03.mot.repository;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.UserAccount;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.DataAccessException;
import pl.lodz.p.it.ssbd2019.ssbd03.repository.AbstractCruRepository;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.roles.MotRoles;

import javax.annotation.security.DenyAll;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import java.util.Optional;

@DenyAll
public class UserAccountRepositoryImpl extends AbstractCruRepository<UserAccount, Long> implements UserAccountRepositoryLocal {
    @Override
    protected EntityManager getEntityManager() {
        return null;
    }

    @Override
    protected Class<UserAccount> getTypeParameterClass() {
        return null;
    }

    @Override
    @RolesAllowed(MotRoles.SHOW_USER_SCORE_HISTORY)
    public Optional<UserAccount> findById(Long id) throws DataAccessException {
        return super.findById(id);
    }
}
