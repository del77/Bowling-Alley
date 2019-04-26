package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web;

import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.service.AccountAccessLevelService;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.service.UserService;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto.EditUserDto;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.AccountAccessLevel;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.User;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.EntityRetrievalException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.EntityUpdateException;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.mvc.Controller;
import javax.mvc.Models;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

/**
 * Kontroler odpowiedzialny za obdługę wszystkich operacji związanych z encjami typu User dla
 * ściezek admina, tj. dla użytkowników o roli "ADMIN"
 */
@Controller
@RequestScoped
@Path("admin/users")
public class UserAdminController {
    @Inject
    private Models models;

    @EJB
    private UserService userService;

    @EJB
    private AccountAccessLevelService accountAccessLevelService;


    /**
     * Zwraca widok z listą wszystkich użytkowników. W wypadku wystąpienia błędu lista jest pusta
     * a użytkownik widzi błąd.
     * @return Widok z listą wszystkich użytkowników.
     */
    @GET
    @Produces(MediaType.TEXT_HTML)
    public String allUsersList() {
        List<User> users = new ArrayList<>();
        try {
            users = userService.getAllUsers();
        } catch (EntityRetrievalException e) {
            models.put("error", "Could not retrieve list of users.\n" + e.getLocalizedMessage());
        }
        models.put("users", users);
        return "accounts/users/userslist.hbs";
    }

    @GET
    @Path("/{id}/edit")
    @Produces(MediaType.TEXT_HTML)
    public String editUser(@PathParam("id") Long id) {
        User user = null;
        try {
            user = userService.getUserById(id);
            models.put("userId", id);
            models.put("login", user.getAccount().getLogin());
            models.put("version", user.getVersion());

        for(AccountAccessLevel accountAccessLevel : user.getAccount().getAccountAccessLevels()) {
            if(accountAccessLevel.getAccessLevel().getName().equals("CLIENT") && accountAccessLevel.isActive()) {
                models.put("clientRole", true);
            }
            else if(accountAccessLevel.getAccessLevel().getName().equals("EMPLOYEE") && accountAccessLevel.isActive()) {
                models.put("employeeRole", true);
            }
            if(accountAccessLevel.getAccessLevel().getName().equals("ADMIN") && accountAccessLevel.isActive()) {
                models.put("adminRole", true);
            }
        }

        } catch (EntityRetrievalException e) {
            models.put("error", "Could not retrieve user.\n" + e.getLocalizedMessage());
        }
        return "accounts/users/editUser.hbs";
    }

    @POST
    @Path("/{id}/edit")
    @Produces(MediaType.TEXT_HTML)
    public String editUser(@BeanParam EditUserDto editUser) {
        List<String> accountAccessLevels = new ArrayList<>();
        if(editUser.isClientRole()) accountAccessLevels.add("CLIENT");
        if(editUser.isEmployeeRole()) accountAccessLevels.add("EMPLOYEE");
        if(editUser.isAdminRole()) accountAccessLevels.add("ADMIN");
        accountAccessLevels.add(AccountAccessLevel.builder().)
//        Account account = Account
//                .builder()
//                .accountAccessLevels()
//                .build();
//        User user = User
//                .builder()
//                .
        xd
        try {
            userService.updateUser(editUser, );
            models.put("updated", true);
        } catch (EntityUpdateException e) {
            models.put("error", "Could not update user.\n" + e.getLocalizedMessage());
        }
        return editUser(editUser.getId());
    }
}
