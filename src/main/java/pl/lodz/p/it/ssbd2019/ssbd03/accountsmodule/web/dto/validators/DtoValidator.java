package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto.validators;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@ApplicationScoped
public class DtoValidator {

    @Inject
    private Validator validator;

    public <T> List<String> validate(T dto) {
        List<String> errors = new ArrayList<>();
        Set<ConstraintViolation<T>> violations = validator.validate(dto);
        for (ConstraintViolation<T> violation : violations) {
            errors.add(violation.getMessage());
        }
        return errors;
    }
}
