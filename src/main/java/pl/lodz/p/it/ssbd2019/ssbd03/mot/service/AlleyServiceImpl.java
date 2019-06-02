package pl.lodz.p.it.ssbd2019.ssbd03.mot.service;

import org.modelmapper.ModelMapper;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.Alley;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;
import pl.lodz.p.it.ssbd2019.ssbd03.mot.repository.AlleyRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.mot.web.dto.AlleyDto;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.roles.MotRoles;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.tracker.InterceptorTracker;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.tracker.TransactionTracker;

import javax.annotation.security.DenyAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import java.util.List;
import java.util.stream.Collectors;

@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Stateful
@DenyAll
@Interceptors(InterceptorTracker.class)
public class AlleyServiceImpl extends TransactionTracker implements AlleyService {
    
    @EJB(beanName = "MOTAlleyRepository")
    private AlleyRepositoryLocal alleyRepositoryLocal;
    
    private ModelMapper modelMapper = new ModelMapper();
    
    @Override
    @RolesAllowed(MotRoles.GET_ALLEYS_LIST)
    public List<AlleyDto> getAllAlleys() throws SsbdApplicationException {
        return alleyRepositoryLocal
                .findAll()
                .stream()
                .map(alley -> modelMapper.map(alley, AlleyDto.class))
                .collect(Collectors.toList());
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
