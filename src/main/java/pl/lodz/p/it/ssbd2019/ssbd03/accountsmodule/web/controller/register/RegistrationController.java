package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.controller.register;


import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.localization.LocalizedMessageRetriever;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.service.RegistrationService;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto.BasicAccountDto;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto.validators.DtoValidator;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto.validators.PasswordDtoValidator;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.UserAccount;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.EntityRetrievalException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.NotUniqueEmailException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.NotUniqueLoginException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.RegistrationProcessException;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.redirect.RedirectUtil;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.mvc.Models;
import java.util.ArrayList;
import java.util.List;

public abstract class RegistrationController {

    @Inject
    protected Models models;

    @Inject
    private PasswordDtoValidator passwordValidator;

    @Inject
    private DtoValidator validator;

    @EJB
    private RegistrationService registrationService;

    protected List<String> errorMessages = new ArrayList<>();

    private static final String ERROR_PREFIX = "errors";
    @Inject
    protected RedirectUtil redirectUtil;

    @Inject
    protected LocalizedMessageRetriever localization;

    static final String SUCCESS_VIEW_URL = "accounts/register/register-success.hbs";

    /**
     * Metoda pomocnicza do uniknięcia duplikowania kodu
     * @param basicAccountDto DTO przechowujące dane formularza rejestracji.
     * @param accessLevelNames poziomy dostepu konta
     * @return Widok potwierdzający rejestrację bądź błąd rejestracji
     */
    String registerAccount(BasicAccountDto basicAccountDto, List<String> accessLevelNames) {
        models.put("data", basicAccountDto);
        errorMessages.addAll(validator.validate(basicAccountDto));
        errorMessages.addAll(passwordValidator.validatePassword(basicAccountDto.getPassword(), basicAccountDto.getConfirmPassword()));

        if (!errorMessages.isEmpty()) {
            return redirectUtil.redirectError(getRegisterEndpointUrl(), basicAccountDto, errorMessages);
        }

        UserAccount userAccount = UserAccount
                .builder()
                .login(basicAccountDto.getLogin())
                .password(basicAccountDto.getPassword())
                .accountConfirmed(false)
                .accountActive(true)
                .email(basicAccountDto.getEmail())
                .firstName(basicAccountDto.getFirstName())
                .lastName(basicAccountDto.getLastName())
                .phone(basicAccountDto.getPhoneNumber())
                .version(0L) // TODO It's workaround for the bug.
                .build();

        try {
            registrationService.registerAccount(userAccount, accessLevelNames);
        } catch (NotUniqueLoginException e) {
            errorMessages.add(localization.get("loginNotUnique"));
        } catch (NotUniqueEmailException e) {
            errorMessages.add(localization.get("emailNotUnique"));
        } catch (RegistrationProcessException | EntityRetrievalException e) {
            errorMessages.add(e.getMessage());
        } catch (Exception e) {
            errorMessages.add(e.getLocalizedMessage() + "\n" + e.getCause());
        }

        if (!errorMessages.isEmpty()) {
            return redirectUtil.redirectError(getRegisterEndpointUrl(), basicAccountDto, errorMessages);
        }

        return String.format("redirect:%s/success", getRegisterEndpointUrl());
    }

    /**
     * funkcja pomocnicza pozwalająca uzyskać url do zwracanego widoku rejestracji
     * @return String url
     */
    protected abstract String getRegisterEndpointUrl();
}
