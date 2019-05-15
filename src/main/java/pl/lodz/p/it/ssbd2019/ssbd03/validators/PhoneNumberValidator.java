package pl.lodz.p.it.ssbd2019.ssbd03.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
    
public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumberFormat, Object> {

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        Field[] fields = o.getClass().getFields();
        for (Field field : fields) {
            ValidPhoneNumberFormat annotation = field.getAnnotation(ValidPhoneNumberFormat.class);
            if (annotation != null) {
                if (field.getType().equals(String.class)) {
                    try {
                        return ((String)field.get(o)).matches("^[0-9]*$");
                    } catch (IllegalAccessException e) {
                        return false;
                    }
                }
            }
        }
        return false;
    }
}
