package pl.lodz.p.it.ssbd2019.ssbd03.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EndDateAfterStartDateValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EndDateAfterStartDate {
    String message() default "{error.dates}";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
