package pl.lodz.p.it.ssbd2019.ssbd03.utils.helpers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.javatuples.Pair;
import pl.lodz.p.it.ssbd2019.ssbd03.mor.web.dto.NewReservationDto;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringTimestampConverter {

    private static Logger logger = Logger.getLogger(StringTimestampConverter.class.getName());
    private static SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");

    public static Optional<Timestamp> getStartDate(NewReservationDto newReservationDto){
        return getDate(newReservationDto.getStartDay(), newReservationDto.getStartHour());
    }

    public static Optional<Timestamp> getEndDate(NewReservationDto newReservationDto){
        return getDate(newReservationDto.getStartDay(), newReservationDto.getEndHour());
    }
    
    public static Pair<Optional<String>, Optional<String>> getDateAndTimeStrings(Timestamp timestamp) {
        if (timestamp != null) {
            String full = format.format(timestamp);
            String date = full.substring(0, full.indexOf(' '));
            String time = full.substring(full.indexOf(' '));
            return new Pair<>(Optional.of(date), Optional.of(time));
        } else {
            return  new Pair<>(Optional.empty(), Optional.empty());
        }
    }
    
    public static Optional<Timestamp> getTimestamp(String date, String hour){
        return getDate(date, hour);
    }

    private static Optional<Timestamp> getDate(String date, String hour) {
        try {
            Date startDate = format.parse(date + " " + hour);
            return Optional.of(new Timestamp(startDate.getTime()));
        } catch (ParseException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return Optional.empty();
    }
}
