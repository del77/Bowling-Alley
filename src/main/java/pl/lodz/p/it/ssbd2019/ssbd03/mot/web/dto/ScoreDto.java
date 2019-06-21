package pl.lodz.p.it.ssbd2019.ssbd03.mot.web.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString
@SuperBuilder
public class ScoreDto extends AddScoreDto {

    @ToString.Exclude
    private Timestamp date;
}
