package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.register;


import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.service.RegistrationService;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto.BasicAccountDto;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto.validators.DtoValidator;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto.validators.PasswordDtoValidator;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.UserAccount;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.EntityRetrievalException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.NotUniqueEmailException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.NotUniqueLoginException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.RegistrationProcessException;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.mvc.Models;
import java.util.List;
import java.util.Optional;

public abstract class RegistrationController {

    @Inject
    private PasswordDtoValidator passwordValidator;

    @Inject
    private DtoValidator validator;

    @Inject
    protected Models models;

    @EJB
    private RegistrationService registrationService;

    @Inject
    protected CacheDto cacheDto;

    private static final String ERROR_PREFIX = "errors";
    static final String SUCCESS_VIEW_URL = "accounts/register/register-success.hbs";

    /**
     * Metoda pomocnicza do uniknięcia duplikowania kodu
     * @param basicAccountDto DTO przechowujące dane formularza rejestracji.
     * @param accessLevelNames poziomy dostepu konta
     * @return Widok potwierdzający rejestrację bądź błąd rejestracji
     */
    protected String registerAccount(BasicAccountDto basicAccountDto, List<String> accessLevelNames) {
        models.put("data", basicAccountDto);
        List<String> errorMessages = validator.validate(basicAccountDto);
        errorMessages.addAll(passwordValidator.validatePassword(basicAccountDto.getPassword(), basicAccountDto.getConfirmPassword()));

        if (!errorMessages.isEmpty()) {
            return handleException(basicAccountDto, errorMessages);
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
            errorMessages.add("Your login is not unique.");
        } catch (NotUniqueEmailException e) {
            errorMessages.add("Your email is not unique.");
        } catch (RegistrationProcessException | EntityRetrievalException e) {
            errorMessages.add(e.getMessage());
        } catch (Exception e) {
            errorMessages.add(e.getLocalizedMessage() + "\n" + e.getCause());
        }

        if (!errorMessages.isEmpty()) {
            return handleException(basicAccountDto, errorMessages);
        }

        return String.format("redirect:%s/success", getRegisterEndpointUrl());
    }

    /**
     * funkcja pomocnicza pozwalająca uzyskać url do zwracanego widoku rejestracji
     * @return String url
     */
    protected abstract String getRegisterEndpointUrl();

    void fillModels(Long id) {
        Optional<FormData> formData = cacheDto.get(id);

        if (formData.isPresent()) {
            models.put("data", formData.get().getBasicAccountDto());
            models.put(ERROR_PREFIX, formData.get().getErrors());
        }
    }

    /**
     * funkcja pomocnicza umieszczająca w widoku dane na temat błędów
     * oraz pozwalająca uzyskać url do zwracanego widoku rejestracji
     * @return String url
     */
    private String handleException(BasicAccountDto basicAccountDto, List<String> errors) {
        Long id = System.currentTimeMillis();
        FormData formData = new FormData(basicAccountDto, errors);
        cacheDto.save(id, formData);
        return String.format("redirect:%s?id=%d", getRegisterEndpointUrl(), id);
    }

}
