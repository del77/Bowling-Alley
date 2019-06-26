package pl.lodz.p.it.ssbd2019.ssbd03.mor.web;

import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.generalized.DataParseException;
import pl.lodz.p.it.ssbd2019.ssbd03.mor.service.ReservationService;
import pl.lodz.p.it.ssbd2019.ssbd03.mor.web.dto.AvailableAlleyDto;
import pl.lodz.p.it.ssbd2019.ssbd03.mor.web.dto.DetailedReservationDto;
import pl.lodz.p.it.ssbd2019.ssbd03.mor.web.dto.ReservationItemDto;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.helpers.StringTimestampConverter;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.localization.LocalizedMessageProvider;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.redirect.FormData;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.redirect.RedirectUtil;

import javax.mvc.Models;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class AbstractReservationController {
    
    private static final String EDIT_RESERVATION_URI_WITHOUT_CONTEXT = "/edit/";
    
    static final String RESERVATION_VIEW = "mor/reservation.hbs";
    private static final String NEW_RESERVATION_BY_CLIENT_VIEW = "mor/newReservation/clientNewReservation.hbs";
    private static final String NEW_RESERVATION_BY_EMPLOYEE_VIEW = "mor/newReservation/employeeNewReservation.hbs";
    static final String RESERVATION_LIST_VIEW = "mor/reservationList.hbs";
    private static final String EDIT_RESERVATION_VIEW = "mor/editReservationForm.hbs";
    
    static final String ERROR = "errors";
    private static final String DATA = "data";
    static final String USERNAME = "userName";
    
    protected abstract ReservationService getReservationService();
    protected abstract Models getModels();
    protected abstract RedirectUtil getRedirectUtil();
    protected abstract LocalizedMessageProvider getLocalization();
    protected abstract String getReservationContext();
    
    String getAvailableAlleys(Long idCache, boolean isClient) {
        getRedirectUtil().injectFormDataToModels(idCache, getModels());
        return isClient ? NEW_RESERVATION_BY_CLIENT_VIEW : NEW_RESERVATION_BY_EMPLOYEE_VIEW;
    }
    
    String injectAvailableAlleys(DetailedReservationDto dto, String redirectTo, Long reservationId, boolean isOwned) {
        try {
            Timestamp startTime = StringTimestampConverter.getTimestamp(dto.getDay(), dto.getStartHour()).orElseThrow(DataParseException::new);
            Timestamp endTime = StringTimestampConverter.getTimestamp(dto.getDay(), dto.getEndHour()).orElseThrow(DataParseException::new);
            dto.setAvailableAlleyNumbers(
                    isOwned ?
                    getReservationService().getAvailableAlleysInTimeRangeExcludingOwnReservation(startTime, endTime)
                            .stream()
                            .map(AvailableAlleyDto::getAlleyNumber)
                            .collect(Collectors.toList()) :
                    getReservationService().getAvailableAlleysInTimeRangeExcludingUserReservation(startTime, endTime)
                            .stream()
                            .map(AvailableAlleyDto::getAlleyNumber)
                            .collect(Collectors.toList())
            );
            setItemsCollectionFromForm(dto);
            switch (redirectTo) {
                case "create":
                    return getAvailableAlleys(null, isOwned); // unused yet
                case "update":
                    return getEditView(reservationId, dto, true, isOwned);
                default:
                    return "pages/index.hbs";
            }
        } catch (SsbdApplicationException e) {
            return getRedirectUtil().redirectError(
                    getReservationContext() + "/details/" + reservationId,
                    null,
                    Collections.singletonList(getLocalization().get(e.getCode())));
        }
    }
    
    String getEditView(Long id, DetailedReservationDto dto, boolean isRedirected, boolean isOwned) {
        try {
            if (getModels().get(DATA) == null && !isRedirected) {
                dto = isOwned ?
                        getReservationService().getOwnReservationById(id, (String) getModels().get(USERNAME)) :
                        getReservationService().getReservationByIdForEdition(id);
            }
            if (dto != null) {
                getModels().put(DATA, dto);
            }
        } catch(SsbdApplicationException e) {
            return getRedirectUtil().redirectError(
                    getReservationContext() + "/details/" + id,
                    null,
                    Collections.singletonList(getLocalization().get(e.getCode())));
        }
        return isRedirected ?
                getRedirectUtil().redirect(
                        getReservationContext() + EDIT_RESERVATION_URI_WITHOUT_CONTEXT + id,
                        FormData.builder()
                                .data(dto)
                                .build()
                ) :
                EDIT_RESERVATION_VIEW;
    }
    
    private void setItemsCollectionFromForm(DetailedReservationDto dto) {
        dto.setItems(
                IntStream.range(0, dto.getSizes().size())
                        .boxed()
                        .map(i -> new ReservationItemDto(
                                dto.getSizes().get(i),
                                dto.getCounts().get(i)
                        ))
                        .collect(Collectors.toList())
        );
    }
}
