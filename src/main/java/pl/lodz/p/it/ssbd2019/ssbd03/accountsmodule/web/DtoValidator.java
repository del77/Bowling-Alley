package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.mvc.Models;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

@RequestScoped
class DtoValidator {

    @Inject
    private Validator validator;
    
    <T> String validate(T dto, Models models) {
        Set<ConstraintViolation<T>> violations = validator.validate(dto);
        models.put("data", dto);
        StringBuilder errorMessage = new StringBuilder();
        for (ConstraintViolation<T> violation : violations) {
            errorMessage.append(violation.getMessage()).append("\n");
        }
        return errorMessage.toString();
    }
}
