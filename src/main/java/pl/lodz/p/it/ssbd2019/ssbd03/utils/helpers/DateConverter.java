package pl.lodz.p.it.ssbd2019.ssbd03.utils.helpers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2019.ssbd03.mor.web.dto.NewReservationDto;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateConverter {

    private static Logger logger = Logger.getLogger(DateConverter.class.getName());
    private static SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");

    public static Optional<Timestamp> getStartDate(NewReservationDto newReservationDto){
        return getDate(newReservationDto, newReservationDto.getStartHour());
    }

    public static Optional<Timestamp> getEndDate(NewReservationDto newReservationDto){
        return getDate(newReservationDto, newReservationDto.getEndHour());
    }

    private static Optional<Timestamp> getDate(NewReservationDto newReservationDto, String hour) {
        try {
            Date startDate = format.parse(newReservationDto.getStartDay() + " " + hour);
            return Optional.of(new Timestamp(startDate.getTime()));
        } catch (ParseException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return Optional.empty();
    }
}
