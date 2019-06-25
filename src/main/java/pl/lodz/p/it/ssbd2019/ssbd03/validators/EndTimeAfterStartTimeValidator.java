package pl.lodz.p.it.ssbd2019.ssbd03.validators;


import pl.lodz.p.it.ssbd2019.ssbd03.mor.web.dto.newReservation.ClientNewReservationDto;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.helpers.StringTimestampConverter;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.sql.Timestamp;
import java.util.Optional;

public class EndTimeAfterStartTimeValidator implements ConstraintValidator<EndTimeAfterStartTime, ClientNewReservationDto> {

    @Override
    public boolean isValid(ClientNewReservationDto newReservationDto, ConstraintValidatorContext constraintValidatorContext) {
        if (hasNullDataFields(newReservationDto)) {
            return false;
        }
        Optional<Timestamp> startDate = StringTimestampConverter.getTimestamp(
                newReservationDto.getStartDay(),
                newReservationDto.getStartHour()
        );
        
        Optional<Timestamp> endDate = StringTimestampConverter.getTimestamp(
                    newReservationDto.getStartDay(),
                    newReservationDto.getEndHour()
        );
    
        if (startDate.isPresent() && endDate.isPresent()) {
            return endDate.get().after(startDate.get());
        } else {
            return false;
        }
    }

    private boolean hasNullDataFields(ClientNewReservationDto newReservationDto) {
        return newReservationDto.getEndHour() == null || newReservationDto.getStartDay() == null || newReservationDto.getStartHour() == null;
    }
}
