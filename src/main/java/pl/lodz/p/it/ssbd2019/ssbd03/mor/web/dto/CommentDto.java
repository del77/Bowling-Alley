package pl.lodz.p.it.ssbd2019.ssbd03.mor.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2019.ssbd03.validators.ValidReservationDates;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ValidReservationDates
public class CommentDto {
    private String content;
    private Timestamp date;
    private boolean active;
}
