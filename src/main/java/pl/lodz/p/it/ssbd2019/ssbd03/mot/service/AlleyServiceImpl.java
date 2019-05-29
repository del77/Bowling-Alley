package pl.lodz.p.it.ssbd2019.ssbd03.mot.service;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.Alley;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.roles.MotRoles;

import javax.annotation.security.RolesAllowed;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.util.List;

@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class AlleyServiceImpl implements AlleyService {
    @Override
    @RolesAllowed(MotRoles.GET_ALLEYS_LIST)
    public List<Alley> getAllAlleys() {
        throw new UnsupportedOperationException();
    }

    @Override
    @RolesAllowed(MotRoles.ADD_ALLEY)
    public void addAlley(Alley alley) {
        throw new UnsupportedOperationException();

    }

    @Override
    @RolesAllowed(MotRoles.ENABLE_DISABLE_ALLEY)
    public void updateLockStatusOnAlleyById(Long id, boolean isActive) {
        throw new UnsupportedOperationException();

    }
}
