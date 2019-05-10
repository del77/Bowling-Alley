package pl.lodz.p.it.ssbd2019.ssbd03.entityvalidators;

import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Documented
@Constraint(validatedBy = ReservationDatesValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidReservationDates {
    String message() default "{error.dates}";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
    
}
