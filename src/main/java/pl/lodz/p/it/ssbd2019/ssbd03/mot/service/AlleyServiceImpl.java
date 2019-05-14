package pl.lodz.p.it.ssbd2019.ssbd03.mot.service;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.Alley;

import javax.annotation.security.RolesAllowed;
import java.util.List;

public class AlleyServiceImpl implements AlleyService {
    @Override
    @RolesAllowed("GetAlleysList")
    public List<Alley> getAllAlleys() {
        throw new UnsupportedOperationException();
    }

    @Override
    @RolesAllowed("AddAlley")
    public void addAlley(Alley alley) {
        throw new UnsupportedOperationException();

    }

    @Override
    @RolesAllowed("EnableDisableAlley")
    public void updateLockStatusOnAlleyById(Long id, boolean isActive) {
        throw new UnsupportedOperationException();

    }
}
