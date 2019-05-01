package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;

@ApplicationScoped
public class DtoValidator {

    @Inject
    private Validator validator;

    public String validate(BasicAccountDto basicAccountDto, List<String> accessLevels) {
        Set<ConstraintViolation<BasicAccountDto>> violations = validator.validate(basicAccountDto);

        StringBuilder errorMessage = new StringBuilder();
        for (ConstraintViolation<BasicAccountDto> violation : violations) {
            errorMessage.append(violation.getMessage()).append("\n");
        }

        if (accessLevels.isEmpty()) {
            errorMessage.append("Please choose any access level.");
        }

        return errorMessage.toString();
    }
}
