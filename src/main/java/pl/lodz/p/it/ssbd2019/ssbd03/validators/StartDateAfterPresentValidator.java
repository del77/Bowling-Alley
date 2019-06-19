package pl.lodz.p.it.ssbd2019.ssbd03.validators;

import pl.lodz.p.it.ssbd2019.ssbd03.mor.web.dto.NewReservationDto;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.helpers.DateConverter;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

public class StartDateAfterPresentValidator implements ConstraintValidator<StartDateAfterPresent, NewReservationDto> {

    @Override
    public boolean isValid(NewReservationDto newReservationDto, ConstraintValidatorContext constraintValidatorContext) {
        Optional<Timestamp> startDateOptional = DateConverter.getStartDate(newReservationDto);
        
        if (startDateOptional.isPresent()) {
            boolean answer = startDateOptional.get().after(Timestamp.from(Instant.now()));
            return answer;
        } else {
            return false;
        }
    }
}
