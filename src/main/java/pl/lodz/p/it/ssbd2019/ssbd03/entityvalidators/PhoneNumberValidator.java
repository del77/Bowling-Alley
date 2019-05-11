package pl.lodz.p.it.ssbd2019.ssbd03.entityvalidators;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.UserAccount;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, UserAccount> {
    
    @Override
    public boolean isValid(UserAccount userAccount, ConstraintValidatorContext constraintValidatorContext) {
        return Pattern.matches("^[0-9]*$", userAccount.getPhone());
    }
}
