package pl.lodz.p.it.ssbd2019.ssbd03.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
    
public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumberFormat, String> {

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext constraintValidatorContext) {
        return phoneNumber.matches("^[0-9]*$");
    }
}
