package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.controller;

import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.localization.LocalizedMessageRetriever;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.service.UserAccountService;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto.AccountActivationDto;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto.ComplexAccountDto;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto.NewPasswordDto;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto.NewPasswordWithConfirmationDto;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto.validators.DtoValidator;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto.validators.PasswordDtoValidator;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.mappers.DtoMapper;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.UserAccount;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.EntityRetrievalException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.EntityUpdateException;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.redirect.FormData;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.redirect.RedirectUtil;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.roles.MokRoles;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.rolesretriever.UserRolesRetriever;
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
    private static final String BASE_PATH = "accounts";
    private static final String DISPLAY_DETAILS = "accounts/users/userDetailsForAdmin.hbs";


    @Inject
    private Models models;
    @Inject
    private DtoValidator validator;
    @Inject
    private PasswordDtoValidator passwordDtoValidator;
    @Inject
    private UserRolesRetriever userRolesRetriever;
    @EJB
    private UserAccountService userAccountService;
    @Inject
    private DtoMapper dtoMapper;
    @Inject
    private RedirectUtil redirectUtil;
    @Inject
    private LocalizedMessageRetriever localization;

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
            displayError(localization.get("userAccountsListError"));
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
        boolean active = dto.getActive() != null; // workaround - checkbox returns null when unchecked
        try {
            UserAccount account = userAccountService.updateLockStatusOnAccountById(dto.getId(), active);
            if (account.isAccountActive() == active) {
                FormData formData = new FormData();
                formData.setInfos(
                        Collections.singletonList(localization.get("unlockedSuccessFullyFor") + account.getLogin())
                );
                return redirectUtil.redirect(BASE_PATH, formData);
            } else {
                return redirectUtil.redirectError(
                        BASE_PATH,
                        null,
                        Collections.singletonList(localization.get("couldntLock") + account.getLogin())
                );
            }
        } catch (Exception e) {
            return redirectUtil.redirectError(
                    BASE_PATH,
                    null,
                    Collections.singletonList(localization.get("couldntLock"))
            );
        }
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
            userRolesRetriever.putAccessLevelsIntoModel(editedAccount,models);
        } catch (Exception e) {
            displayError(localization.get("userDetailsNotUpdated"));
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
            userRolesRetriever.putAccessLevelsIntoModel(user,models);
        } catch (EntityRetrievalException e) {
            displayError(localization.get("userCouldntRetrieve"));
         }
        return DISPLAY_DETAILS;
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
    public String editUser(@BeanParam ComplexAccountDto editUser, @PathParam("id") Long id, @QueryParam("idCache") Long idCache) {
        try {
            List<String> selectedAccessLevels = dtoMapper.getListOfAccessLevels(editUser);
            userAccountService.updateUserWithAccessLevels(editedAccount, selectedAccessLevels);
            models.put("updated", true);
        } catch (EntityUpdateException e) {
            return redirectUtil.redirectError(
                    String.format("%s/%d/edit", BASE_PATH, id),
                    null,
                    Collections.singletonList(localization.get("userDetailsNotUpdated"))
            );
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
            return redirectUtil.redirectError(
                    String.format("%s/%d/edit/password", BASE_PATH, id),
                    null,
                    errorMessages);
        }

        try {
            userAccountService.changePasswordById(id, userData.getNewPassword());
        } catch (Exception e) {
            return redirectUtil.redirectError(
                    String.format("%s/%d/edit/password", BASE_PATH, id),
                    null,
                    Collections.singletonList(e.getMessage()));

        }

        return String.format("redirect:%s/success", BASE_PATH);
    }

    private String redirectSuccessPath() {
        return String.format("redirect:%s/success", BASE_PATH);
    }

    private void displayError(String s) {
        models.put(ERROR, Collections.singletonList(s));
    }
}
