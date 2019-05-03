package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.register;


import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.service.RegistrationService;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto.BasicAccountDto;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto.DtoValidator;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.UserAccount;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.EntityRetrievalException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.NotUniqueEmailException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.NotUniqueLoginException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.RegistrationProcessException;

import javax.mvc.Models;
import java.util.List;

public abstract class RegistrationController {

    private static final String ERROR_PREFIX = "errors";

    /**
     * Metoda pomocnicza do uniknięcia duplikowania kodu
     * @param basicAccountDto DTO przechowujące dane formularza rejestracji.
     * @param accessLevelNames poziomy dostepu konta
     * @return Widok potwierdzający rejestrację bądź błąd rejestracji
     */
    String registerAccount(BasicAccountDto basicAccountDto, List<String> accessLevelNames) {
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
            getRegistrationService().registerAccount(userAccount, accessLevelNames);
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
            return handleException(errorMessages);
        }

        return "accounts/register/register-success.hbs";
    }

    private String handleException(List<String> errors) {
        getModels().put(ERROR_PREFIX, errors);
        return getRegisterViewUrl();
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

    /**
     * funkcja pomocnicza pozwalająca uzyskać url do zwracanego widoku rejestracji
     * @return String url
     */
    protected abstract String getRegisterViewUrl();
}
