package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.register;

import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto.BasicAccountDto;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;

@ApplicationScoped
class DtoValidator {

    @Inject
    private Validator validator;

    String validate(BasicAccountDto basicAccountDto, List<String> accessLevels) {
        Set<ConstraintViolation<BasicAccountDto>> violations = validator.validate(basicAccountDto);

        String errorMessage = "";
        for (ConstraintViolation<BasicAccountDto> violation : violations) {
            errorMessage += violation.getMessage() + "\n";
        }

        if (accessLevels.isEmpty()) {
            errorMessage += "Please choose any access level.";
        }

        return errorMessage;
    }
}
