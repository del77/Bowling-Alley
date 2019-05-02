package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

@ApplicationScoped
public class DtoValidator {

    @Inject
    private Validator validator;

    public String validate(BasicAccountDto basicAccountDto) {
        Set<ConstraintViolation<BasicAccountDto>> violations = validator.validate(basicAccountDto);

        StringBuilder errorMessage = new StringBuilder();
        for (ConstraintViolation<BasicAccountDto> violation : violations) {
            errorMessage.append(violation.getMessage()).append("\n");
        }

        return errorMessage.toString();
    }
}
