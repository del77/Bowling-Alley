package pl.lodz.p.it.ssbd2019.ssbd03.mor.web.dto.newReservation;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2019.ssbd03.mor.web.dto.AvailableAlleyDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewReservationAllForm {
    private Object newReservationDto;
    private List<AvailableAlleyDto> availableAlleys;
    private String selfUrl;
}
