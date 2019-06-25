package pl.lodz.p.it.ssbd2019.ssbd03.validators;


import pl.lodz.p.it.ssbd2019.ssbd03.mor.web.dto.ClientNewReservationDto;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.helpers.StringTimestampConverter;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

public class StartDateAfterPresentValidator implements ConstraintValidator<StartDateAfterPresent, ClientNewReservationDto> {

    @Override
    public boolean isValid(ClientNewReservationDto newReservationDto, ConstraintValidatorContext constraintValidatorContext) {
        if (hasNullDataFields(newReservationDto)) {
            return false;
        }

        Optional<Timestamp> startDateOptional = StringTimestampConverter.getStartDate(newReservationDto);
    
        return startDateOptional.map(timestamp -> timestamp.after(Timestamp.from(Instant.now()))).orElse(false);
    }

    private boolean hasNullDataFields(ClientNewReservationDto newReservationDto) {
        return newReservationDto.getEndHour() == null || newReservationDto.getStartDay() == null || newReservationDto.getStartHour() == null;
    }
}
