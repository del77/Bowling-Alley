package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web;

import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.service.UserAccountService;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto.AdminEditPasswordDto;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto.ComplexAccountDto;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto.DtoValidator;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto.UserEditPasswordDto;
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

    @Inject
    private Models models;

    @Inject
    private DtoValidator validator;

    @EJB
    private UserAccountService userAccountService;

    @Inject
    private DtoMapper dtoMapper;

    private UserAccount editedAccount;

    /**
     * Zwraca widok z listą wszystkich użytkowników. W wypadku wystąpienia błędu lista jest pusta
     * a użytkownik widzi błąd.
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
     * Zwraca widok z formularzem edycji użytkownika.
     * @return Widok z formularzem edycji uzytkownika.
     */
    @GET
    @Path("/{id}/edit")
    @Produces(MediaType.TEXT_HTML)
    public String editUser(@PathParam("id") Long id) {
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
     * Odpowiada za edycję danych użytkownika oraz poziomów jego dostępu.
     * @return Informacja o rezultacie edycji.
     */
    @POST
    @Path("/{id}/edit")
    @Produces(MediaType.TEXT_HTML)
    public String editUser(@BeanParam ComplexAccountDto editUser) {
        try {
            List<String> selectedAccessLevels = dtoMapper.getListOfAccessLevels(editUser);
            userAccountService.updateUserWithAccessLevels(editedAccount, selectedAccessLevels);
            models.put("updated", true);
        } catch (EntityUpdateException e) {
            displayError("There was a problem during user update.\n", e.getLocalizedMessage());
        }
        return editUser(editedAccount.getId());
    }


    /**
     * Odblokowuje konto użytkownika z podanym identyfikatorem i zwraca true, jeśli operacja się powiedzie
     *
     * @param id id konta, które należy odblokować
     * @return true, jeśli odblokowanie konta się powiedzie
     */
    @PUT
    @Path("unlock/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Boolean unlockAccount(@PathParam("id") Long id) {
        try {
            userAccountService.unlockAccountById(id);
        } catch (Exception e) {
            displayError("Could not unlock user's account.\n", e.getLocalizedMessage());
            return false;
        }
        return true;
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
    public String editUserSPassword() {
        return "accounts/edit-password/editByAdmin.hbs";
    }


    /**
     * Punkt wyjścia odpowiedzialny za zmianę hasła użytkownika oraz przekierowanie do strony o statusie.
     *
     * @param userData DTO przechowujące dane formularza edycji hasła.
     * @return Widok potwierdzający aktualizację hasła lub komunikat o błędzie
     * @see UserEditPasswordDto
     */
    @POST
    @Path("/{id}/edit/password")
    @Produces(MediaType.TEXT_HTML)
    public String editUserSPassword(@BeanParam AdminEditPasswordDto userData, @PathParam("id") Long id) {
        List<String> errorMessages = validator.validate(userData);
        errorMessages.addAll(validator.validatePasswordsEquality(userData.getNewPassword(), userData.getConfirmNewPassword()));

        if (!errorMessages.isEmpty()) {
            models.put(ERROR, errorMessages);
            return "accounts/edit-password/editByAdmin.hbs";
        }

        try {
            userAccountService.changePasswordByAdmin(id, userData.getNewPassword());
        } catch (Exception e) {
            errorMessages.add(e.getMessage());
            models.put(ERROR, errorMessages);
            return "accounts/edit-password/editByUser.hbs";
        }

        return "accounts/edit-password/success.hbs";
    }

    private void putAccessLevelsIntoModel(UserAccount userAccount){
        for(AccountAccessLevel accountAccessLevel : userAccount.getAccountAccessLevels()) {
            if(accountAccessLevel.isActive()) {
                switch(accountAccessLevel.getAccessLevel().getName()){
                    case "CLIENT":
                        models.put("clientActive",true);
                        break;
                    case "EMPLOYEE":
                        models.put("employeeActive",true);
                        break;
                    case "ADMIN":
                        models.put("adminActive",true);
                        break;
                }
            }
        }
    }

    private void displayError(String s, String localizedMessage) {
        models.put(ERROR, Collections.singletonList(s + localizedMessage));
    }

}
