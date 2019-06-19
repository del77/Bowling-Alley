package pl.lodz.p.it.ssbd2019.ssbd03.validators;

import pl.lodz.p.it.ssbd2019.ssbd03.mor.web.dto.NewReservationDto;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.helpers.DateConverter;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.sql.Timestamp;
import java.util.Optional;

public class EndTimeAfterStartTimeValidator implements ConstraintValidator<EndTimeAfterStartTime, NewReservationDto> {

    @Override
    public boolean isValid(NewReservationDto newReservationDto, ConstraintValidatorContext constraintValidatorContext) {
        Optional<Timestamp> startDateOptional = DateConverter.getStartDate(newReservationDto);
        Optional<Timestamp> endDateOptional = DateConverter.getEndDate(newReservationDto);


        if (startDateOptional.isPresent() && endDateOptional.isPresent()) {
            return endDateOptional.get().after(startDateOptional.get());
        } else {
            return false;
        }
    }
}
