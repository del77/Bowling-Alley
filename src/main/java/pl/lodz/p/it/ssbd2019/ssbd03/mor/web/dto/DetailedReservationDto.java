package pl.lodz.p.it.ssbd2019.ssbd03.mor.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2019.ssbd03.validators.EndDateAfterStartDate;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.ws.rs.FormParam;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EndDateAfterStartDate(message = "{validate.endTimeAfterStartTime}")
public class DetailedReservationDto {
    
    @NotEmpty(message = "{validate.startDateIsNull}")
    @FormParam("startDay")
    private String startDay;
    
    @NotEmpty(message = "{validate.startHourIsEmpty}")
    @FormParam("startHour")
    private String startHour;
    
    @NotNull(message = "{validate.endDateIsNull}")
    @FormParam("endDay")
    private String endDay;
    
    @NotEmpty(message = "{validate.endHourIsEmpty}")
    @FormParam("endHour")
    private String endHour;
    
    @Min(value = 1, message = "{validate.playersCountBelow1}")
    @FormParam("numberOfPlayers")
    private int numberOfPlayers;
    
    @FormParam("alleyNumber")
    private int alleyNumber;
    
    @FormParam("size")
    private List<@Min(1) Integer> sizes;
    
    @FormParam("count")
    private List<@Min(1) Integer> counts;
    
    private List<Integer> availableAlleyNumbers;
    
    private List<ReservationItemDto> items;
}
