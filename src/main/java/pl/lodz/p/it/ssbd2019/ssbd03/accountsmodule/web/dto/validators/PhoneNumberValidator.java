package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto.validators;


import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto.PhoneNumberDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, PhoneNumberDto> {
    
    @Override
    public boolean isValid(PhoneNumberDto userAccount, ConstraintValidatorContext constraintValidatorContext) {
        return Pattern.matches("^[0-9]*$", userAccount.getPhoneNumber());
    }
}
