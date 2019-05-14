package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.controller;

import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.service.UserAccountService;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto.AccountActivationDto;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto.ComplexAccountDto;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto.NewPasswordDto;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto.NewPasswordWithConfirmationDto;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto.validators.DtoValidator;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto.validators.PasswordDtoValidator;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.mappers.DtoMapper;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.AccountAccessLevel;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.UserAccount;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.EntityRetrievalException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.EntityUpdateException;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.redirect.FormData;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.redirect.RedirectUtil;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Kontroler odpowiedzialny za obdługę wszystkich operacji związanych z encjami typu UserAccount dla
 * ściezek admina, tj. dla użytkowników o roli "ADMIN"
 */
@Controller
@SessionScoped
@Path("accounts")
public class UserAdminController implements Serializable {

    private static final String ERROR = "errors";
    private static final String EDIT_PASSWORD_FORM_HBS = "accounts/edit-password/editByAdmin.hbs";
    private static final String EDIT_SUCCESS_VIEW = "accounts/edit-password/edit-success.hbs";
    private static final String BASE_PATH = "admin/users";

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
    @Inject
    private RedirectUtil redirectUtil;

    private transient UserAccount editedAccount;

    @GET
    @Path("/success")
    @Produces(MediaType.TEXT_HTML)
    public String success(@QueryParam("idCache") Long id) {
        return EDIT_SUCCESS_VIEW;
    }

    /**
     * Zwraca widok z listą wszystkich użytkowników. W wypadku wystąpienia błędu lista jest pusta
     * a użytkownik widzi błąd.
     *
     * @return Widok z listą wszystkich użytkowników.
     */
    @GET
    @RolesAllowed(MokRoles.GET_ALL_USERS_LIST)
    @Produces(MediaType.TEXT_HTML)
    public String allUsersList(@QueryParam("idCache") Long id) {
        redirectUtil.injectFormDataToModels(id, models);
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
    @RolesAllowed(MokRoles.LOCK_UNLOCK_ACCOUNT)
    @Produces(MediaType.TEXT_HTML)
    public String updateLockStatusOnAccount(@BeanParam AccountActivationDto dto,
                                            @QueryParam("idCache") Long id) {
        FormData formData = new FormData();
        boolean active = dto.getActive() != null; // workaround - checkbox returns null when unchecked
        try {
            UserAccount account = userAccountService.updateLockStatusOnAccountById(dto.getId(), active);
            if(account.isAccountActive() == active) {
                formData.setInfos(
                        Collections.singletonList(String.format("Successfully changed %s's lock state.", account.getLogin()))
                );
                return redirectUtil.redirect(BASE_PATH, formData);
            } else {
                formData.setErrors(
                        Collections.singletonList(String.format("Could not change %s's lock state", account.getLogin()))
                );
            }
        } catch (Exception e) {
            formData.setErrors(
                    Collections.singletonList("Could not change user's lock state" + e.getLocalizedMessage())
            );
        }
        return redirectUtil.redirect(BASE_PATH, formData);
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
    public String editUser(@PathParam("id") Long id, @QueryParam("idCache") Long idCache) {
        redirectUtil.injectFormDataToModels(idCache, models);
        try {
            editedAccount = userAccountService.getUserById(id);
            models.put("id", editedAccount.getId());
            models.put("login", editedAccount.getLogin());
            putAccessLevelsIntoModel(editedAccount);
        } catch (Exception e) {
            displayError("Could not update user.\n", e.getLocalizedMessage());
        }
        return "accounts/users/editUser.hbs";
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
    public String displayUserDetails(@PathParam("id") Long id, @QueryParam("idCache") Long idCache) {
        redirectUtil.injectFormDataToModels(idCache, models);
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
    @RolesAllowed(MokRoles.CHANGE_USER_PASSWORD)
    @Produces(MediaType.TEXT_HTML)
    public String editUserPassword(@QueryParam("idCache") Long idCache) {
        redirectUtil.injectFormDataToModels(idCache, models);
        return EDIT_PASSWORD_FORM_HBS;
    }

    /**
     * Odpowiada za edycję danych użytkownika oraz poziomów jego dostępu.
     *
     * @return Informacja o rezultacie edycji.
     */
    @POST
    @Path("/{id}/edit")
    @Produces(MediaType.TEXT_HTML)
    public String editUser(@BeanParam ComplexAccountDto editUser, @QueryParam("idCache") Long idCache) {
        try {
            List<String> selectedAccessLevels = dtoMapper.getListOfAccessLevels(editUser);
            userAccountService.updateUserWithAccessLevels(editedAccount, selectedAccessLevels);
            models.put("updated", true);
        } catch (EntityUpdateException e) {
            return redirectUtil.redirectError(BASE_PATH + "/{id}/edit", null, Collections.singletonList("There was a problem during user update.\n" + e.getLocalizedMessage()));

        }
        return redirectSuccessPath();
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
    @RolesAllowed(MokRoles.CHANGE_USER_PASSWORD)
    @Produces(MediaType.TEXT_HTML)
    public String editUserPassword(@BeanParam NewPasswordDto userData, @PathParam("id") Long id, @QueryParam("idCache") Long idCache) {
        List<String> errorMessages = validator.validate(userData);
        errorMessages.addAll(passwordDtoValidator.validatePassword(userData.getNewPassword(), userData.getConfirmNewPassword()));

        if (!errorMessages.isEmpty()) {
            return redirectUtil.redirectError(BASE_PATH + "/{id}/edit/password", null, errorMessages);
        }

        try {
            userAccountService.changePasswordById(id, userData.getNewPassword());
        } catch (Exception e) {
            return redirectUtil.redirectError(BASE_PATH + "/{id}/edit/password", null, Collections.singletonList(e.getMessage()));
        }

        return String.format("redirect:%s/success", BASE_PATH);
    }

    private String redirectSuccessPath() {
        return String.format("redirect:%s/success", BASE_PATH);
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

    private void displayError(String s, String localizedMessage) {
        models.put(ERROR, Collections.singletonList(s + localizedMessage));
    }

}
