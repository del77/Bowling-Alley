package pl.lodz.p.it.ssbd2019.ssbd03.mor.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2019.ssbd03.validators.ValidReservationDates;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ValidReservationDates
public class ReservationFullDto {
    private Long id;
    private Long userId;
    private Timestamp startDate;
    private Timestamp endDate;
    private int playersCount;
    private boolean active;
    private int alleyNumber;
    private List<CommentDto> comments;
}
