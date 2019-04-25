package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web;

import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.service.AccessLevelService;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.service.AccountAccessLevelService;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.service.UserService;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto.EditUserDto;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.AccountAccessLevel;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.User;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.EntityRetrievalException;

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

    @EJB
    private AccessLevelService accessLevelService;

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
        List<AccountAccessLevel> accountAccessLevels = null;
        try {
            user = userService.getUserById(id);
            models.put("login", user.getAccount().getLogin());

            //accountAccessLevels = accountAccessLevelService.getAccountAccessLevelsByUserId(Long.parseLong(id));
            accountAccessLevels = accountAccessLevelService.getAccountAccessLevelsByUserId((id));
        } catch (EntityRetrievalException e) {
            models.put("error", "Could not retrieve user.\n" + e.getLocalizedMessage());
        }
        for(AccountAccessLevel accountAccessLevel : accountAccessLevels) {
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
        models.put("userId", id);
        //if(accountAccessLevels)
        //models.put("accountAccessLevels", accountAccessLevels);
        return "accounts/users/editUser.hbs";
    }

    @POST
    @Path("/{id}/edit")
    @Produces(MediaType.TEXT_HTML)
    public String editUser(@BeanParam EditUserDto editUser) {
        //todo uzupelnic usera
        List<AccountAccessLevel> accountAccessLevels = new ArrayList<>();

        try {
            User user = userService.getUserById(editUser.getId());
            AccountAccessLevel clientAccessLevel = AccountAccessLevel.builder()
                    .account(user.getAccount())
                    .accessLevel(accessLevelService.getAccessLevelByName("CLIENT"))
                    .active(editUser.isClientRole())
                    .build();
            AccountAccessLevel employeeAccessLevel = AccountAccessLevel.builder()
                    .account(user.getAccount())
                    .accessLevel(accessLevelService.getAccessLevelByName("EMPLOYEE"))
                    .active(editUser.isEmployeeRole())
                    .build();
            AccountAccessLevel adminAccessLevel= AccountAccessLevel.builder()
                    .account(user.getAccount())
                    .accessLevel(accessLevelService.getAccessLevelByName("ADMIN"))
                    .active(editUser.isAdminRole())
                    .build();
            accountAccessLevels.add(clientAccessLevel);
            accountAccessLevels.add(employeeAccessLevel);
            accountAccessLevels.add(adminAccessLevel);
            userService.updateUser(user, accountAccessLevels);
        } catch (Exception e) {

        }
        models.put("updated", true);
        return editUser(editUser.getId());
    }
}
