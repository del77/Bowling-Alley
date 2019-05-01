package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web;


import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.service.RegistrationService;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto.BasicAccountDto;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.UserAccount;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.EntityRetrievalException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.NotUniqueParameterException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.RegistrationProcessException;

import javax.mvc.Models;
import java.util.List;

public abstract class RegistrationController {

    private static final String ERROR_PREFIX = "error";
    private static final String REGISTER_VIEW_URL = "accounts/register/registerByAdmin.hbs";

    /**
     * Metoda pomocnicza do uniknięcia duplikowania kodu
     * @param basicAccountDto DTO przechowujące dane formularza rejestracji.
     * @param accessLevelName poziom dostepu konta
     * @return Widok potwierdzający rejestrację bądź błąd rejestracji
     */
    String registerAccount(BasicAccountDto basicAccountDto, String accessLevelName) {
        getModels().put("data", basicAccountDto);
        List<String> errorMessages = getValidator().validate(basicAccountDto);
        errorMessages.addAll(getValidator().validatePasswordsEquality(basicAccountDto.getPassword(), basicAccountDto.getConfirmPassword()));

        if (!errorMessages.isEmpty()) {
            return handleException(errorMessages);
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
                .accountsVersion(0L)
                .build();

        try {
            getRegistrationService().registerAccount(userAccount, accessLevelName);
        } catch (NotUniqueParameterException e) {
            errorMessages.add("Your email or login is not unique.");
        } catch (RegistrationProcessException | EntityRetrievalException e) {
            errorMessages.add(e.getMessage());
        } catch (Exception e) {
            errorMessages.add(e.getLocalizedMessage() + "\n" + e.getCause());
        }

        if (!errorMessages.isEmpty()) {
            return handleException(errorMessages);
        }

        return "accounts/register/register-success.hbs";
    }

    protected String handleException(List<String> errors) {
        getModels().put(ERROR_PREFIX, errors);
        return REGISTER_VIEW_URL;
    }


    /**
     * funkcja pomocnicza pozwalająca uzyskać dostęp do wstrzykniętych obiektów klasie bazowej
     * @return modele mvc
     */
    protected abstract Models getModels();

    /**
     * funkcja pomocnicza pozwalająca uzyskać dostęp do wstrzykniętych obiektów klasie bazowej
     * @return validator dto
     */
    protected abstract DtoValidator getValidator();

    /**
     * funkcja pomocnicza pozwalająca uzyskać dostęp do wstrzykniętych obiektów klasie bazowej
     * @return serwis RegistrationService
     */
    protected abstract RegistrationService getRegistrationService();
}
