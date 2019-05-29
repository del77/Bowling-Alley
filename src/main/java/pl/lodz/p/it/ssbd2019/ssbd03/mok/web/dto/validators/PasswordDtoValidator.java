package pl.lodz.p.it.ssbd2019.ssbd03.mok.web.dto.validators;

import pl.lodz.p.it.ssbd2019.ssbd03.utils.localization.LocalizedMessageProvider;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class PasswordDtoValidator {

    @Inject
    private LocalizedMessageProvider localization;

    public List<String> validatePassword(String password, String confirmPassword) {
        List<String> errors = new ArrayList<>();

        if (!areEqual(password, confirmPassword)) {
            errors.add(localization.get("validate.passwordMismatch"));
        }

        return errors;
    }

    public List<String> validateCurrentAndNewPassword(String currentPassword, String newPassword) {
        List<String> errors = new ArrayList<>();

        if (areEqual(currentPassword, newPassword)) {
            errors.add(localization.get("validate.differentOldAndNew"));
        }

        return errors;
    }


    private boolean areEqual(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }
}
