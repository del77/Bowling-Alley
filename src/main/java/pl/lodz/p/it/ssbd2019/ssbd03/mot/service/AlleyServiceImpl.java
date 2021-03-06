package pl.lodz.p.it.ssbd2019.ssbd03.mot.service;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.Alley;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.AlleyDoesNotExistException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.DataAccessException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.notfound.NotFoundException;
import pl.lodz.p.it.ssbd2019.ssbd03.mot.repository.AlleyRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.mot.web.dto.AlleyCreationDto;
import pl.lodz.p.it.ssbd2019.ssbd03.mot.web.dto.AlleyDto;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.helpers.Mapper;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.roles.MotRoles;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.tracker.InterceptorTracker;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.tracker.TransactionTracker;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import java.util.List;

@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Stateful(name = "MOTAlleyService")
@DenyAll
@Interceptors(InterceptorTracker.class)
public class AlleyServiceImpl extends TransactionTracker implements AlleyService {


    @EJB(beanName = "MOTAlleyRepository")
    AlleyRepositoryLocal alleyRepositoryLocal;



    @Override
    @RolesAllowed(MotRoles.GET_ALLEYS_LIST)
    public List<AlleyDto> getAllAlleys() throws SsbdApplicationException {
        return Mapper.mapAll(alleyRepositoryLocal.findAll(), AlleyDto.class);
    }


    @Override
    @RolesAllowed({MotRoles.ADD_SERVICE_REQUEST, MotRoles.GET_ALLEYS_LIST, MotRoles.GET_BEST_SCORE_FOR_ALLEY})
    public AlleyDto getById(Long id) throws SsbdApplicationException {
        return Mapper.map(
                alleyRepositoryLocal.findById(id).orElseThrow(NotFoundException::new),
                AlleyDto.class
        );
    }

    @Override
    @RolesAllowed(MotRoles.ADD_ALLEY)
    public void addAlley(AlleyCreationDto alleyDto) throws SsbdApplicationException {
        Alley alley = Alley
                .builder()
                .number(alleyDto.getNumber())
                .maxScore(0)
                .active(true)
                .version(0L)
                .build();
        alleyRepositoryLocal.create(alley);
    }

    @Override
    @RolesAllowed(MotRoles.ENABLE_DISABLE_ALLEY)
    public void updateLockStatusOnAlleyById(Long id, boolean isActive) throws SsbdApplicationException{
        Alley alley = alleyRepositoryLocal.findById(id).orElseThrow(
                () -> new AlleyDoesNotExistException("Alley with id '" + id + "' does not exist."));
        alley.setActive(isActive);
        alleyRepositoryLocal.editWithoutMerge(alley);
    }

}
