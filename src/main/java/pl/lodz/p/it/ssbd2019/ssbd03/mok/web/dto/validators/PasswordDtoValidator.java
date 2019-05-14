package pl.lodz.p.it.ssbd2019.ssbd03.mok.web.dto.validators;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class PasswordDtoValidator {

    public List<String> validatePassword(String password, String confirmPassword) {
        List<String> errors = new ArrayList<>();

        if (!areEqual(password, confirmPassword)) { errors.add("Passwords don't match."); }
        // ...

        return errors;
    }

    private boolean areEqual(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }
}