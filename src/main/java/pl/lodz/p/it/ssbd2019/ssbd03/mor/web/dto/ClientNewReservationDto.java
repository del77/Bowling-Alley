package pl.lodz.p.it.ssbd2019.ssbd03.mor.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2019.ssbd03.validators.EndTimeAfterStartTime;
import pl.lodz.p.it.ssbd2019.ssbd03.validators.StartDateAfterPresent;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.ws.rs.FormParam;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EndTimeAfterStartTime(message = "{validate.endTimeAfterStartTime}")
@StartDateAfterPresent(message = "{validate.startDateBeforePresent}")
public class ClientNewReservationDto {

    @NotEmpty(message = "{validate.startDateIsNull}")
    @FormParam("startDay")
    protected String startDay;
    
    @NotEmpty(message = "{validate.startHourIsEmpty}")
    @FormParam("startHour")
    protected String startHour;

    @NotEmpty(message = "{validate.endHourIsEmpty}")
    @FormParam("endHour")
    protected String endHour;
    
    @Min(value = 1, message = "{validate.playersCountBelow1}")
    @FormParam("numberOfPlayers")
    protected int numberOfPlayers;
}
