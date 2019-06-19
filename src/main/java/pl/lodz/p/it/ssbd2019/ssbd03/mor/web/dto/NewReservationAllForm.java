package pl.lodz.p.it.ssbd2019.ssbd03.mor.web.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewReservationAllForm {
    private NewReservationDto newReservationDto;
    private List<AvailableAlleyDto> availableAlleys;
}
