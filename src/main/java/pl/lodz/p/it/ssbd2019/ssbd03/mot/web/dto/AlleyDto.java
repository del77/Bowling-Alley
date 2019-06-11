package pl.lodz.p.it.ssbd2019.ssbd03.mot.web.dto;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.ws.rs.FormParam;

@Data
@NoArgsConstructor
public class AlleyDto {
    @NotNull
    @Getter
    private Long id;
    
    @NotNull(message = "{validate.alleyNumberIsNull}")
    @Min(value = 0, message = "{validate.alleyNumberConstraint}")
    @FormParam("number")
    private int number;
    
    @FormParam("active")
    private boolean active;
    
    @NotNull(message = "{validate.bestScoreIsNull}")
    @Min(value = 0, message = "{validate.bestScoreLowerThanZero}")
    @Max(value = 300, message = "{validate.bestScoreHigherThan300}")
    @FormParam("maxScore")
    @ToString.Exclude
    private int maxScore;
}
