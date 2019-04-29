package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web;

import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.service.UserAccountService;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto.AccountDetailsDto;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.mappers.EditUserDtoMapper;
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
import java.util.List;

/**
 * Kontroler odpowiedzialny za obdługę wszystkich operacji związanych z encjami typu UserAccount dla
 * ściezek admina, tj. dla użytkowników o roli "ADMIN"
 */
@Controller
@SessionScoped
@Path("admin/users")
public class UserAdminController implements Serializable {
    private static final String ERROR = "error";
    
    @Inject
    private Models models;

    @EJB
    private UserAccountService userAccountService;
    
    @Inject
    private DtoValidator validator;

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
            models.put(ERROR, "Could not retrieve list of userAccounts.\n" + e.getLocalizedMessage());
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
    public String editUserPage(@PathParam("id") Long id) {
        try {
            UserAccount editedAccount = userAccountService.getUserById(id);
            models.put("editedAccount", editedAccount);
            for(AccountAccessLevel accountAccessLevel : editedAccount.getAccountAccessLevels()) {
                if(accountAccessLevel.isActive()) {
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
        } catch (Exception e) {
            models.put(ERROR, "Could not update user.\n" + e.getLocalizedMessage());
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
    public String editUserPage(@BeanParam AccountDetailsDto editUser, @PathParam("id") Long id) {
        editUser.setId(id);
        String errorMessage = validator.validate(editUser, models);
    
        if (!errorMessage.isEmpty()) {
            models.put(ERROR, errorMessage);
            return editUserPage(id);
        }
        
        try {
            List<String> selectedAccessLevels = EditUserDtoMapper.editUserDtoToListOfAccessLevels(editUser);
            userAccountService.updateUserAccountDetails(editUser, selectedAccessLevels);
            models.put("updated", true);
        } catch (EntityUpdateException e) {
            models.put(ERROR, "There was a problem during user update.\n" + e.getLocalizedMessage());
        } catch (Exception e) {
            models.put(ERROR, e.getMessage());
        }
        return editUserPage(id);
    }


    /**
     * Odblokowuje konto użytkownika z podanym identyfikatorem i zwraca true, jeśli operacja się powiedzie
     *
     * @param id id konta, które należy odblokować
     * @return true, jeśli odblokowanie konta się powiedzie
     */
    @PUT
    @Path("unlock/{id}")
    @Produces(MediaType.TEXT_HTML)
    public String unlockAccount(@PathParam("id") Long id) {
        try {
            userAccountService.unlockAccountById(id);
        } catch (Exception e) {
            models.put(ERROR, "Could not unlock user's account.\n" + e.getLocalizedMessage());
            return allUsersList();
        }
        return allUsersList();
    }
}
