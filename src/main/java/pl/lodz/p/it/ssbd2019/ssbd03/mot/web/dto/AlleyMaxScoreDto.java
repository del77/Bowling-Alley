package pl.lodz.p.it.ssbd2019.ssbd03.mot.web.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlleyMaxScoreDto {
    Integer score;
}
