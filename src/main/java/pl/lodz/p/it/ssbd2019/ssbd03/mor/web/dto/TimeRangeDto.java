package pl.lodz.p.it.ssbd2019.ssbd03.mor.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import javax.ws.rs.FormParam;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeRangeDto {

    @NotNull(message = "{validate.startDateIsNull}")
    @FutureOrPresent(message = "{validate.startDateBeforePresent}")
    @FormParam("startDate")
    private Timestamp startDate;

    @Min(1)
    @Max(10)
    @FormParam("numberOfHours")
    private Integer numberOfHours;
}
