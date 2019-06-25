package pl.lodz.p.it.ssbd2019.ssbd03.validators;

import pl.lodz.p.it.ssbd2019.ssbd03.mor.web.dto.DetailedReservationDto;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.helpers.StringTimestampConverter;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.sql.Timestamp;
import java.util.Optional;

public class EndDateAfterStartDateValidator implements ConstraintValidator<EndDateAfterStartDate, DetailedReservationDto> {
    
    @Override
    public boolean isValid(DetailedReservationDto dto, ConstraintValidatorContext constraintValidatorContext) {
        if (hasNullDataFields(dto)) {
            return false;
        }
        Optional<Timestamp> startDate = StringTimestampConverter.getTimestamp(
                dto.getDay(),
                dto.getStartHour()
        );
        
        Optional<Timestamp> endDate = StringTimestampConverter.getTimestamp(
                dto.getDay(),
                dto.getEndHour()
        );
        
        if (startDate.isPresent() && endDate.isPresent()) {
            return endDate.get().after(startDate.get());
        } else {
            return false;
        }
    }
    
    private boolean hasNullDataFields(DetailedReservationDto dto) {
        return dto.getEndHour() == null || dto.getDay() == null || dto.getStartHour() == null;
    }
}