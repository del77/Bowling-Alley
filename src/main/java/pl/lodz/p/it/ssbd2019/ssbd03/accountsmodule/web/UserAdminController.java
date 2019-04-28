package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web;

import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.service.UserAccountService;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto.EditUserDto;
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
    @Inject
    private Models models;

    @EJB
    private UserAccountService userAccountService;

    UserAccount editedAccount;

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
            models.put("error", "Could not retrieve list of userAccounts.\n" + e.getLocalizedMessage());
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
            models.put("login", editedAccount.getLogin());

        for(AccountAccessLevel accountAccessLevel : editedAccount.getAccountAccessLevels()) {
            if(accountAccessLevel.getAccessLevel().getName().equals("CLIENT")) {
                models.put("clientVersion", accountAccessLevel.getVersion());
                if(accountAccessLevel.isActive()) models.put("clientActive", true);
            }
            else if(accountAccessLevel.getAccessLevel().getName().equals("EMPLOYEE")) {
                models.put("employeeVersion", accountAccessLevel.getVersion());
                if(accountAccessLevel.isActive()) models.put("employeeActive", true);
            }
            if(accountAccessLevel.getAccessLevel().getName().equals("ADMIN")) {
                models.put("adminVersion", accountAccessLevel.getVersion());
                if(accountAccessLevel.isActive()) models.put("adminActive", true);
            }
        }
        } catch (Exception e) {
            models.put("error", "Could not update user.\n" + e.getLocalizedMessage());
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
    public String editUser(@BeanParam EditUserDto editUser) {
        try {
            List<String> selectedAccessLevels = new ArrayList<>();
            if(editUser.isClientRoleSelected()) selectedAccessLevels.add("CLIENT");
            if(editUser.isEmployeeRoleSelected()) selectedAccessLevels.add("EMPLOYEE");
            if(editUser.isAdminRoleSelected()) selectedAccessLevels.add("ADMIN");

            userAccountService.updateUser(editedAccount, selectedAccessLevels);
            models.put("updated", true);
        } catch (EntityUpdateException e) {
            models.put("error", "There wsa a problem during user update.\n" + e.getLocalizedMessage());
        }
        return editUser(editedAccount.getId());
    }

}
