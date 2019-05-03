package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto.validators;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class PasswordDtoValidator {

    public List<String> validatePasswordsEquality(String password, String confirmPassword) {
        List<String> errors = new ArrayList<>();
        if (!password.equals(confirmPassword)) {
            errors.add("Passwords don't match.");
        }

        return errors;
    }
}