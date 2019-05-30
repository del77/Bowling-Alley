package pl.lodz.p.it.ssbd2019.ssbd03.mok.web.controller;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.UserAccount;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;
import pl.lodz.p.it.ssbd2019.ssbd03.mok.service.UserAccountService;
import pl.lodz.p.it.ssbd2019.ssbd03.mok.web.dto.*;
import pl.lodz.p.it.ssbd2019.ssbd03.mok.web.dto.validators.DtoValidator;
import pl.lodz.p.it.ssbd2019.ssbd03.mok.web.dto.validators.PasswordDtoValidator;
import pl.lodz.p.it.ssbd2019.ssbd03.mok.web.mappers.DtoMapper;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.localization.LocalizedMessageProvider;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.redirect.FormData;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.redirect.RedirectUtil;
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

    // ================ PATHS =========================
    private static final String BASE_PATH = "accounts";
    private static final String EDIT_USER_PATH = "accounts/%d/edit";
    private static final String EDIT_USER_ROLES_PATH = "accounts/%d/edit/roles";

    // ================= VIEWS ========================
    private static final String DISPLAY_DETAILS_VIEW = "accounts/users/userDetailsForAdmin.hbs";
    private static final String EDIT_PASSWORD_FORM_VIEW = "accounts/edit-password/editByAdmin.hbs";
    private static final String EDIT_SUCCESS_VIEW = "accounts/edit-password/edit-success.hbs";
    private static final String EDIT_USER_ROLES_VIEW = "accounts/users/editUserRoles.hbs";
    private static final String EDIT_USER_VIEW = "accounts/users/editUser.hbs";
    private static final String USER_LIST_VIEW = "accounts/users/userslist.hbs";


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
    @Inject
    private LocalizedMessageProvider localization;

    @GET
    @Path("/success")
    @Produces(MediaType.TEXT_HTML)
    public String success(@QueryParam("idCache") Long id) {
        return EDIT_SUCCESS_VIEW;
    }

    /**
     * Zwraca widok z listą wszystkich użytkowników, lub z listą użytkowników,
     * których imię lub nazwisko zawiera podany ciąg znaków.
     * W wypadku wystąpienia błędu lista jest pusta
     * a użytkownik widzi błąd.
     *
     * @param name Ciąg znaków, który ma być obecny w imieniu lub nazwisku użytkowników.
     * @return Widok z listą użytkowników.
     */
    @GET
    @RolesAllowed(MokRoles.GET_ALL_USERS_LIST)
    @Produces(MediaType.TEXT_HTML)
    public String listUsers(
            @QueryParam("idCache") Long id,
            @DefaultValue("") @QueryParam("name") String name) {
        redirectUtil.injectFormDataToModels(id, models);
        List<UserAccount> userAccounts = new ArrayList<>();
        try {
            if (name.equals("") || name == null) {
                userAccounts = userAccountService.getAllUsers();
            } else {
                userAccounts = userAccountService.getAllByNameOrLastName(name);
            }
        } catch (SsbdApplicationException e) {
            displayError(localization.get(e.getCode()) + "\n" + localization.get("userAccountsListError"));
        }
        models.put("userAccounts", userAccounts);
        return USER_LIST_VIEW;
    }

    /**
     * Zmienia status zablokowania konta użytkownika z podanym identyfikatorem
     *
     * @param dto     dto z id konta, któremu należy zmienić flagę aktywności
     * @param idCache opcjonalny, identyfikator cache z danymi po przekierowaniu
     * @return Widok z listą użytkowników oraz komunikatem o powodzeniu lub błędzie
     */
    @POST
    @RolesAllowed(MokRoles.LOCK_UNLOCK_ACCOUNT)
    @Produces(MediaType.TEXT_HTML)
    public String updateLockStatusOnAccount(@BeanParam AccountActivationDto dto,
                                            @QueryParam("idCache") Long idCache) {
        boolean active = dto.getActive() != null; // workaround - checkbox returns null when unchecked
        try {
            userAccountService.updateLockStatusOnAccountById(dto.getId(), active);
            FormData formData = new FormData();
            String message = active ? localization.get("unlockedSuccess") : localization.get("lockedSuccess");
            formData.setInfos(Collections.singletonList(message));
            return redirectUtil.redirect(BASE_PATH, formData);
        } catch (SsbdApplicationException e) {
            return redirectUtil.redirectError(
                    BASE_PATH,
                    null,
                    Collections.singletonList(localization.get(e.getCode()) + "\n" + localization.get("couldntLock"))
            );
        }
    }


    /**
     * Zwraca widok z formularzem edycji użytkownika.
     *
     * @param id      identyfikator użytkownika do edycji
     * @param idCache opcjonalny, identyfikator cache z danymi po przekierowaniu
     * @return Widok z formularzem edycji uzytkownika.
     */
    @GET
    @Path("/{id}/edit")
    @RolesAllowed(MokRoles.EDIT_USER_ACCOUNT)
    @Produces(MediaType.TEXT_HTML)
    public String editUser(@PathParam("id") Long id, @QueryParam("idCache") Long idCache) {
        redirectUtil.injectFormDataToModels(idCache, models);
        try {
            AccountDetailsDto editedAccount = userAccountService.getUserById(id);
            models.put("editedAccount", editedAccount);
        } catch (SsbdApplicationException e) {
            displayError(localization.get("userCouldntRetrieve"));
        }
        return EDIT_USER_VIEW;
    }


    /**
     * Odpowiada za edycję danych użytkownika.
     *
     * @param dto dto z danymi
     * @return Informacja o rezultacie edycji.
     */
    @POST
    @Path("/{id}/edit")
    @RolesAllowed(MokRoles.EDIT_USER_ACCOUNT)
    @Produces(MediaType.TEXT_HTML)
    public String editUser(@BeanParam AccountDetailsDto dto, @PathParam("id") Long userId) {
        List<String> errorMessages = validator.validate(dto);

        if (!errorMessages.isEmpty()) {
            return redirectUtil.redirectError(
                    String.format(EDIT_USER_PATH, userId),
                    dto,
                    errorMessages);
        }

        try {
            userAccountService.updateUser(dto);
        } catch (SsbdApplicationException e) {
            return redirectUtil.redirectError(
                    String.format(EDIT_USER_PATH, userId),
                    dto,
                    Collections.singletonList(localization.get(e.getCode()))
            );
        }

        FormData formData = FormData.builder()
                .data(dto)
                .infos(Collections.singletonList(String.format(
                        "%s",
                        localization.get("userDetailsUpdated"))
                ))
                .build();
        return redirectUtil.redirect(String.format(EDIT_USER_PATH, userId), formData);
    }

    /**
     * Zwraca widok z formularzem edycji poziomów dostępu użytkownika.
     *
     * @param id      identyfikator użytkownika do edycji
     * @param idCache opcjonalny, identyfikator cache z danymi po przekierowaniu
     * @return Widok z formularzem edycji poziomów dostępu użytkownika.
     */
    @GET
    @Path("/{id}/edit/roles")
    @RolesAllowed(MokRoles.EDIT_USER_ACCOUNT)
    @Produces(MediaType.TEXT_HTML)
    public String editUserRoles(@PathParam("id") Long id, @QueryParam("idCache") Long idCache) {
        redirectUtil.injectFormDataToModels(idCache, models);
        try {
            AccountDetailsDto editedAccount = userAccountService.getUserById(id);
            models.put("editedAccount", editedAccount);
        } catch (SsbdApplicationException e) {
            displayError(localization.get("userCouldntRetrieveRoles"));
        }
        return EDIT_USER_ROLES_VIEW;
    }

    /**
     * Odpowiada za edycję poziomów dostępu użytkownika.
     *
     * @param dto dto z danymi
     * @return Informacja o rezultacie edycji.
     */
    @POST
    @Path("/{id}/edit/roles")
    @RolesAllowed(MokRoles.EDIT_USER_ACCOUNT)
    @Produces(MediaType.TEXT_HTML)
    public String editUserRoles(@BeanParam UserRolesDto dto, @PathParam("id") Long userId) {
        List<String> errorMessages = validator.validate(dto);

        if (!errorMessages.isEmpty()) {
            return redirectUtil.redirectError(
                    String.format(EDIT_USER_PATH, userId),
                    dto,
                    errorMessages);
        }

        try {
            List<String> selectedAccessLevels = dtoMapper.getListOfAccessLevels(dto);
            userAccountService.updateUserAccessLevels(dto, selectedAccessLevels);
        } catch (SsbdApplicationException e) {
            return redirectUtil.redirectError(
                    String.format(EDIT_USER_ROLES_PATH, userId),
                    dto,
                    Collections.singletonList(localization.get("rolesNotUpdated"))
            );
        }
        FormData formData = FormData.builder()
                .data(dto)
                .infos(Collections.singletonList(String.format(
                        "%s",
                        localization.get("rolesUpdated")
                )))
                .build();
        return redirectUtil.redirect(String.format(EDIT_USER_ROLES_PATH, userId), formData);
    }

    /**
     * Zwraca widok z danymi użytkownika o podanym ID.
     *
     * @param id      id konta, którego dane mają zostać wyświetlone
     * @param idCache opcjonalny, identyfikator cache z danymi po przekierowaniu
     * @return widok z danymi użytkownika o podanym ID.
     */
    @GET
    @Path("/{id}/details")
    @RolesAllowed(MokRoles.GET_USER_DETAILS)
    @Produces(MediaType.TEXT_HTML)
    public String displayUserDetails(@PathParam("id") Long id, @QueryParam("idCache") Long idCache) {
        redirectUtil.injectFormDataToModels(idCache, models);
        try {
            AccountDetailsDto user = userAccountService.getUserById(id);
            models.put("user", user);
        } catch (SsbdApplicationException e) {
            displayError(localization.get("userCouldntRetrieve"));
        }
        return DISPLAY_DETAILS_VIEW;
    }

    /**
     * Punkt wyjścia odpowiedzialny za przekierowanie do widoku z formularzem edycji hasła dla użytkownika.
     *
     * @param idCache opcjonalny, identyfikator cache z danymi po przekierowaniu
     * @return Widok z formularzem zmiany hasła dla użytkownika
     */
    @GET
    @Path("/{id}/edit/password")
    @RolesAllowed(MokRoles.CHANGE_USER_PASSWORD)
    @Produces(MediaType.TEXT_HTML)
    public String editUserPassword(@QueryParam("idCache") Long idCache) {
        redirectUtil.injectFormDataToModels(idCache, models);
        return EDIT_PASSWORD_FORM_VIEW;
    }


    /**
     * Punkt wyjścia odpowiedzialny za zmianę hasła użytkownika oraz przekierowanie do strony o statusie.
     *
     * @param id       identyfikator użytkownika do edycji hasła
     * @param userData DTO przechowujące dane formularza edycji hasła.
     * @param idCache  opcjonalny, identyfikator cache z danymi po przekierowaniu
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
        } catch (SsbdApplicationException e) {
            return redirectUtil.redirectError(
                    String.format("%s/%d/edit/password", BASE_PATH, id),
                    null,
                    Collections.singletonList(localization.get(e.getCode())));

        }

        return redirectSuccessPath();
    }

    private String redirectSuccessPath() {
        return String.format("redirect:%s/success", BASE_PATH);
    }

    private void displayError(String s) {
        models.put(ERROR, Collections.singletonList(s));
    }

}
