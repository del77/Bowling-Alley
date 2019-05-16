package pl.lodz.p.it.ssbd2019.ssbd03.validators;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.Reservation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ReservationDatesValidator implements ConstraintValidator<ValidReservationDates, Reservation> {
    @Override
    public boolean isValid(Reservation reservation, ConstraintValidatorContext constraintValidatorContext) {
        return reservation.getEndDate().after(reservation.getStartDate());
    }
}
