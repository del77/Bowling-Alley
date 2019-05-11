package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web;

import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.service.UserAccountService;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto.*;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto.validators.DtoValidator;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto.validators.PasswordDtoValidator;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.mappers.DtoMapper;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.AccountAccessLevel;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.UserAccount;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.EntityRetrievalException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.EntityUpdateException;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.mvc.Controller;
import javax.mvc.Models;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Kontroler odpowiedzialny za obdługę wszystkich operacji związanych z encjami typu UserAccount dla
 * ściezek admina, tj. dla użytkowników o roli "ADMIN"
 */
@Controller
@SessionScoped
@Path("admin/users")
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
    @Produces(MediaType.TEXT_HTML)
    public String allUsersList() {
        List<UserAccount> userAccounts = new ArrayList<>();
        try {
            userAccounts = userAccountService.getAllUsers();
        } catch (EntityRetrievalException e) {
            displayError("Could not retrieve list of userAccounts.\n", e.getLocalizedMessage());
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
    @Produces(MediaType.TEXT_HTML)
    public String updateLockStatusOnAccount(@BeanParam AccountActivationDto dto) {
        boolean active = dto.getActive() != null; // workaround - checkbox returns null when unchecked
        try {
            UserAccount account = userAccountService.updateLockStatusOnAccountById(dto.getId(), active);
            if(account.isAccountActive() == active) {
                models.put(INFO, Collections.singletonList(
                        String.format("Successfully changed %s's lock state.", account.getLogin())));
            } else {
                displayError(String.format("Could not change %s's lock state", account.getLogin()), "");
            }
        } catch (Exception e) {
            displayError("Could not change user's lock state", e.getLocalizedMessage());
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
    @Produces(MediaType.TEXT_HTML)
    public String editUser(@PathParam("id") Long id) {
        try {
            editedAccount = userAccountService.getUserById(id);
            models.put("editedAccount", editedAccount);
            putAccessLevelsIntoModel(editedAccount);
        } catch (Exception e) {
            displayError("Could not update user.\n", e.getLocalizedMessage());
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
    @Produces(MediaType.TEXT_HTML)
    public String editUser(@BeanParam AccountDetailsDto dto) {
        List<String> errorMessages = validator.validate(dto);
    
        if(!errorMessages.isEmpty()) {
            for (String errorMessage : errorMessages) {
                displayError(errorMessage, "");
            }
            return editUser(editedAccount.getId());
        }

        try {
            List<String> selectedAccessLevels = dtoMapper.getListOfAccessLevels(dto);
            editedAccount = userAccountService.updateUserAccountDetails(editedAccount, dto, selectedAccessLevels);
            models.put(INFO, Collections.singletonList(
                    String.format("Successfully updated %s", editedAccount.getLogin())));
        } catch (EntityUpdateException e) {
            displayError("There was a problem during user update.\n", e.getMessage());
        } catch (Exception e) {
            displayError("FATAL ERROR", e.getLocalizedMessage());
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
    @Produces(MediaType.TEXT_HTML)
    public String displayUserDetails(@PathParam("id") Long id) {
        try {
            UserAccount user = userAccountService.getUserById(id);
            models.put("user", user);
            putAccessLevelsIntoModel(user);
        } catch (EntityRetrievalException e) {
            displayError("Could not retrieve user.\n", e.getLocalizedMessage());
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
    @Produces(MediaType.TEXT_HTML)
    public String editUserPassword() {
        return EDIT_PASSWORD_FORM_HBS;
    }


    /**
     * Punkt wyjścia odpowiedzialny za zmianę hasła użytkownika oraz przekierowanie do strony o statusie.
     *
     * @param userData DTO przechowujące dane formularza edycji hasła.
     * @return Widok potwierdzający aktualizację hasła lub komunikat o błędzie
     * @see NewPasswordWithConfirmationDto
     */
    @POST
    @Path("/{id}/edit/password")
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

        models.put(INFO, Collections.singletonList("Password has been changed."));
        return EDIT_PASSWORD_FORM_HBS;
    }

    private void putAccessLevelsIntoModel(UserAccount userAccount) {
        for (AccountAccessLevel accountAccessLevel : userAccount.getAccountAccessLevels()) {
            if (accountAccessLevel.isActive()) {
                switch (accountAccessLevel.getAccessLevel().getName()) {
                    case "CLIENT":
                        models.put("clientActive", true);
                        break;
                    case "EMPLOYEE":
                        models.put("employeeActive", true);
                        break;
                    case "ADMIN":
                        models.put("adminActive", true);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private void displayError(String s, String localizedMessage) {
        models.put(ERROR, Collections.singletonList(s + localizedMessage));
    }

}
