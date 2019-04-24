package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web;

import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto.BasicAccountDto;

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

    String validate(BasicAccountDto basicAccountDto, Models models) {
        Set<ConstraintViolation<BasicAccountDto>> violations = validator.validate(basicAccountDto);
        models.put("data", basicAccountDto);
        String errorMessage = "";
        for (ConstraintViolation<BasicAccountDto> violation : violations) {
            errorMessage += violation.getMessage() + "\n";
        }
        return errorMessage;
    }
}
