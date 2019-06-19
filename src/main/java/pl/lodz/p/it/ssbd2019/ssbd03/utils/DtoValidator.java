package pl.lodz.p.it.ssbd2019.ssbd03.utils;

import pl.lodz.p.it.ssbd2019.ssbd03.utils.ValidatorConfig;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@ApplicationScoped
public class DtoValidator {

    @Inject
    private ValidatorConfig config;

    public <T> List<String> validate(T dto) {
        List<String> errors = new ArrayList<>();
        Set<ConstraintViolation<T>> violations = config.validator().validate(dto);
        for (ConstraintViolation<T> violation : violations) {
            errors.add(violation.getMessage());
        }
        return errors;
    }

    public <T> List<String> validateAll(List<T> dtos) {
        List<String> errors = new ArrayList<>();
        for(T dto : dtos) {
            errors.addAll(validate(dto));
        }
        return errors;
    }
}
