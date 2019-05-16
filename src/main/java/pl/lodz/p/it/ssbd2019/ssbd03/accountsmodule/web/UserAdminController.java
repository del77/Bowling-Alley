package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web;

import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.localization.LocalizedMessageRetriever;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.service.UserAccountService;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto.AccountActivationDto;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto.NewPasswordDto;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto.validators.DtoValidator;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto.NewPasswordWithConfirmationDto;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto.validators.PasswordDtoValidator;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.mappers.DtoMapper;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto.*;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.AccountAccessLevel;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.UserAccount;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.EntityRetrievalException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.EntityUpdateException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.NotUniqueEmailException;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.roles.AppRoles;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.roles.MokRoles;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.mvc.Controller;
import javax.mvc.Models;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.Serializable;
import java.util.*;

/**
 * Kontroler odpowiedzialny za obdługę wszystkich operacji związanych z encjami typu UserAccount dla
 * ściezek admina, tj. dla użytkowników o roli "ADMIN"
 */
@Controller
@SessionScoped
@Path("accounts")
public class UserAdminController implements Serializable {

    private static final String ERROR = "errors";
    private static final String INFO = "infos";
    private static final String EDIT_PASSWORD_FORM_HBS = "accounts/edit-password/editByAdmin.hbs";

    @Inject
    private Models models;

    @Inject
    private DtoValidator validator;
    @Inject
    private PasswordDtoValidator passwordDtoValidator;
    @Inject
    private LocalizedMessageRetriever localizedMessageRetriever;

    @EJB
    private UserAccountService userAccountService;

    @Inject
    private DtoMapper dtoMapper;

    private transient UserAccount editedAccount;

    /**
     * Zwraca widok z listą wszystkich użytkowników. W wypadku wystąpienia błędu lista jest pusta
     * a użytkownik widzi błąd.
     *
     * @return Widok z listą wszystkich użytkowników.
     */
    @GET
    @RolesAllowed(MokRoles.GET_ALL_USERS_LIST)
    @Produces(MediaType.TEXT_HTML)
    public String allUsersList() {
        List<UserAccount> userAccounts = new ArrayList<>();
        try {
            userAccounts = userAccountService.getAllUsers();
        } catch (EntityRetrievalException e) {
            displayError(localizedMessageRetriever.getLocalizedMessage("userAccountsListError"));
        }
        models.put("userAccounts", userAccounts);
        return "accounts/users/userslist.hbs";
    }
    
    /**
     * Zmienia status zablokowania konta użytkownika z podanym identyfikatorem
     *
     * @param dto dto z id konta, któremu należy zmienić flagę aktywności
     * @return Widok z listą użytkowników oraz komunikatem o powodzeniu lub błędzie
     */
    @POST
    @RolesAllowed(MokRoles.LOCK_UNLOCK_ACCOUNT)
    @Produces(MediaType.TEXT_HTML)
    public String updateLockStatusOnAccount(@BeanParam AccountActivationDto dto) {
        boolean active = dto.getActive() != null; // workaround - checkbox returns null when unchecked
        try {
            UserAccount account = userAccountService.updateLockStatusOnAccountById(dto.getId(), active);
            if(account.isAccountActive() == active) {
                models.put(INFO, Collections.singletonList(
                        String.format("%s. %s", localizedMessageRetriever.getLocalizedMessage("changeLockStatusSuccess"), account.getLogin())));
            } else {
                displayError(String.format("%s. %s", localizedMessageRetriever.getLocalizedMessage("changeLockStatusFailure"), account.getLogin()));
            }
        } catch (Exception e) {
            displayError(localizedMessageRetriever.getLocalizedMessage("changeLockStatusFailure"));
        }
        return allUsersList();
    }


    /**
     * Zwraca widok z formularzem edycji użytkownika.
     *
     * @return Widok z formularzem edycji uzytkownika.
     */
    @GET
    @Path("/{id}/edit")
    @RolesAllowed(MokRoles.EDIT_USER_ACCOUNT)
    @Produces(MediaType.TEXT_HTML)
    public String editUser(@PathParam("id") Long id) {
        try {
            editedAccount = userAccountService.getUserById(id);
            models.put("editedAccount", editedAccount);
            putAccessLevelsIntoModel(editedAccount);
        } catch (Exception e) {
            displayError(localizedMessageRetriever.getLocalizedMessage("userDetailsNotUpdated"));
        }
        return "accounts/users/editUser.hbs";
    }


    /**
     * Odpowiada za edycję danych użytkownika oraz poziomów jego dostępu.
     *
     * @return Informacja o rezultacie edycji.
     */
    @POST
    @Path("/{id}/edit")
    @RolesAllowed(MokRoles.EDIT_USER_ACCOUNT)
    @Produces(MediaType.TEXT_HTML)
    public String editUser(@BeanParam AccountDetailsDto dto) {
        List<String> errorMessages = validator.validate(dto);
    
        if(!errorMessages.isEmpty()) {
            for (String errorMessage : errorMessages) {
                displayError(errorMessage);
            }
            return editUser(editedAccount.getId());
        }

        try {
            editedAccount.setFirstName(dto.getFirstName());
            editedAccount.setLastName(dto.getLastName());
            editedAccount.setEmail(dto.getEmail());
            editedAccount.setPhone(dto.getPhoneNumber());
            List<String> selectedAccessLevels = dtoMapper.getListOfAccessLevels(dto);
            editedAccount = userAccountService.updateUserWithAccessLevels(editedAccount, selectedAccessLevels);
            models.put(INFO, Collections.singletonList(localizedMessageRetriever.getLocalizedMessage("profileUpdatedSuccessfully")));
        } catch (EntityUpdateException e) {
            displayError(localizedMessageRetriever.getLocalizedMessage("profileUpdatedUnsuccessfully"));
        } catch (NotUniqueEmailException e) {
            displayError(localizedMessageRetriever.getLocalizedMessage("notUniqueEmailException"));
        } catch (Exception e) {
            displayError(localizedMessageRetriever.getLocalizedMessage("FATAL"));
        }
        return editUser(editedAccount.getId());
    }

    /**
     * Zwraca widok z danymi użytkownika o podanym ID.
     *
     * @param id id konta, którego dane mają zostać wyświetlone
     * @return widok z danymi użytkownika o podanym ID.
     */
    @GET
    @Path("/{id}/details")
    @RolesAllowed(MokRoles.GET_USER_DETAILS)
    @Produces(MediaType.TEXT_HTML)
    public String displayUserDetails(@PathParam("id") Long id) {
        try {
            UserAccount user = userAccountService.getUserById(id);
            models.put("user", user);
            putAccessLevelsIntoModel(user);
        } catch (EntityRetrievalException e) {
            displayError(localizedMessageRetriever.getLocalizedMessage("userRetrievalError"));
        }
        return "accounts/users/userDetails.hbs";
    }

    /**
     * Punkt wyjścia odpowiedzialny za przekierowanie do widoku z formularzem edycji hasła dla użytkownika.
     *
     * @return Widok z formularzem zmiany hasła dla użytkownika
     */
    @GET
    @Path("/{id}/edit/password")
    @RolesAllowed(MokRoles.CHANGE_USER_PASSWORD)
    @Produces(MediaType.TEXT_HTML)
    public String editUserPassword() {
        return EDIT_PASSWORD_FORM_HBS;
    }


    /**
     * Punkt wyjścia odpowiedzialny za zmianę hasła użytkownika oraz przekierowanie do strony o statusie.
     * @param userData DTO przechowujące dane formularza edycji hasła.
     * @return Widok potwierdzający aktualizację hasła lub komunikat o błędzie
     * @see NewPasswordWithConfirmationDto
     */
    @POST
    @Path("/{id}/edit/password")
    @RolesAllowed(MokRoles.CHANGE_USER_PASSWORD)
    @Produces(MediaType.TEXT_HTML)
    public String editUserPassword(@BeanParam NewPasswordDto userData, @PathParam("id") Long id) {
        List<String> errorMessages = validator.validate(userData);
        errorMessages.addAll(passwordDtoValidator.validatePassword(userData.getNewPassword(), userData.getConfirmNewPassword()));

        if (!errorMessages.isEmpty()) {
            models.put(ERROR, errorMessages);
            return EDIT_PASSWORD_FORM_HBS;
        }

        try {
            userAccountService.changePasswordById(id, userData.getNewPassword());
        } catch (Exception e) {
            models.put(ERROR, Collections.singletonList(e.getMessage()));
            return EDIT_PASSWORD_FORM_HBS;
        }

        models.put(INFO, Collections.singletonList(
                localizedMessageRetriever.getLocalizedMessage("passwordChanged")));
        return EDIT_PASSWORD_FORM_HBS;
    }

    private void putAccessLevelsIntoModel(UserAccount userAccount) {
        for (AccountAccessLevel accountAccessLevel : userAccount.getAccountAccessLevels()) {
            if (accountAccessLevel.isActive()) {
                switch (accountAccessLevel.getAccessLevel().getName()) {
                    case AppRoles.CLIENT:
                        models.put("clientActive", true);
                        break;
                    case AppRoles.EMPLOYEE:
                        models.put("employeeActive", true);
                        break;
                    case AppRoles.ADMIN:
                        models.put("adminActive", true);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private void displayError(String s) {
        models.put(ERROR, Collections.singletonList(s));
    }
}
