package pl.lodz.p.it.ssbd2019.ssbd03.utils.helpers;

import pl.lodz.p.it.ssbd2019.ssbd03.mor.web.dto.ReservationFullDto;

import java.sql.Timestamp;
import java.time.Instant;

public class ReservationValidator {
    private static final long MILLISECONDS_IN_SECOND = 1000;
    private static final long SECONDS_IN_MINUTE = 60;
    private static final long MINUTES_IN_HOUR = 60;
    private static final int HOURS_TO_RESERVATION_START_DATE = 2;


    public static boolean isExpired(ReservationFullDto reservation) {
        long startDate = reservation.getStartDate().getTime();
        long now = Timestamp.from(Instant.now()).getTime();
        long hoursToStart =  (startDate - now) / (MILLISECONDS_IN_SECOND * SECONDS_IN_MINUTE * MINUTES_IN_HOUR);
        return hoursToStart < HOURS_TO_RESERVATION_START_DATE;
    }
}
