package pl.lodz.p.it.ssbd2019.ssbd03.mor.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2019.ssbd03.validators.ValidReservationDates;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.ws.rs.FormParam;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ValidReservationDates
public class ReservationDto {

    @NotNull(message = "{validate.startDateIsNull}")
    @FutureOrPresent(message = "{validate.startDateBeforePresent}")
    @FormParam("startDate")
    private Timestamp startDate;

    @NotNull(message = "{validate.endDateIsNull}")
    @Future(message = "{validate.startDateBeforePresent}")
    @FormParam("endDate")
    private Timestamp endDate;

    @Min(value = 1, message = "{validate.playersCountBelow1}")
    @FormParam("playersCount")
    private int playersCount;

    @FormParam("active")
    private boolean active;

    @FormParam("alleyNumber")
    private int alleyNumber;

}
