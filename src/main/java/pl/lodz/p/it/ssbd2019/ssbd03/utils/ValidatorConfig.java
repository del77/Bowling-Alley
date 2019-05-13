package pl.lodz.p.it.ssbd2019.ssbd03.utils;

import pl.lodz.p.it.ssbd2019.ssbd03.utils.configuration.i18n.I18nMessageInterpolator;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;


@ApplicationScoped
public class ValidatorConfig {

    @Inject
    private I18nMessageInterpolator messageInterpolator;

    private Validator validator = null;

    public Validator validator() {
        if (validator == null) {
            ValidatorFactory factory = Validation
                    .byDefaultProvider()
                    .configure()
                    .buildValidatorFactory();
            this.validator = factory
                    .usingContext()
                    .messageInterpolator(messageInterpolator)
                    .getValidator();
        }
        return this.validator;
    }
}
