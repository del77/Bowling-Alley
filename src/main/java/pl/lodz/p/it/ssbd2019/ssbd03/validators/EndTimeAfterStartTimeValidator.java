package pl.lodz.p.it.ssbd2019.ssbd03.validators;

import pl.lodz.p.it.ssbd2019.ssbd03.mor.web.dto.NewReservationDto;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.helpers.StringToTimestampConverter;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.sql.Timestamp;
import java.util.Optional;

public class EndTimeAfterStartTimeValidator implements ConstraintValidator<EndTimeAfterStartTime, NewReservationDto> {

    @Override
    public boolean isValid(NewReservationDto newReservationDto, ConstraintValidatorContext constraintValidatorContext) {
        if (hasNullDataFields(newReservationDto)) {
            return false;
        }

        Optional<Timestamp> startDateOptional = StringToTimestampConverter.getStartDate(newReservationDto);
        Optional<Timestamp> endDateOptional = StringToTimestampConverter.getEndDate(newReservationDto);

        if (startDateOptional.isPresent() && endDateOptional.isPresent()) {
            return endDateOptional.get().after(startDateOptional.get());
        } else {
            return false;
        }
    }

    private boolean hasNullDataFields(NewReservationDto newReservationDto) {
        return newReservationDto.getEndHour() == null || newReservationDto.getStartDay() == null || newReservationDto.getStartHour() == null;
    }
}
