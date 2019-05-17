package pl.lodz.p.it.ssbd2019.ssbd03.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PhoneNumberValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PhoneNumberFormat {
    String message() default "{error.phoneNumber}";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
