package pl.lodz.p.it.ssbd2019.ssbd03.mor.service;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.ReservationItem;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;
import pl.lodz.p.it.ssbd2019.ssbd03.mor.repository.ReservationItemRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.mor.web.dto.ReservationItemsDto;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.roles.MorRoles;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.tracker.InterceptorTracker;

import javax.annotation.security.DenyAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import java.util.List;
import java.util.stream.Collectors;

@Stateful(name = "MORReservationItemService")
@Interceptors(InterceptorTracker.class)
@DenyAll
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class ReservationItemServiceImpl implements ReservationItemService {
    
    @EJB(beanName = "MORReservationItemRepository")
    private ReservationItemRepositoryLocal reservationItemRepositoryLocal;
    
    @Override
    @RolesAllowed({MorRoles.EDIT_OWN_RESERVATION, MorRoles.EDIT_RESERVATION_FOR_USER,
            MorRoles.GET_OWN_RESERVATION_DETAILS, MorRoles.GET_RESERVATION_DETAILS})
    public ReservationItemsDto getItemsForReservation(long reservationId) throws SsbdApplicationException {
        List<ReservationItem> reservationItems = reservationItemRepositoryLocal.getItemsForReservation(reservationId);
        return ReservationItemsDto.builder()
                .size(reservationItems.stream().map(ri -> ri.getItem().getSize()).collect(Collectors.toList()))
                .count(reservationItems.stream().map(ReservationItem::getCount).collect(Collectors.toList()))
                .build();
    }
    
}
