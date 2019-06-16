package pl.lodz.p.it.ssbd2019.ssbd03.mot.web.dto;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.FormParam;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ScoreDto {

    @FormParam("login")
    private String login;

    @FormParam("score")
    @Min(value = 0, message = "{validate.scoreMin}")
    @Max(value = 300, message = "{validate.scoreMax}")
    private int score;
}