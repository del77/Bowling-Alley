package pl.lodz.p.it.ssbd2019.ssbd03.web.servlets;

import pl.lodz.p.it.ssbd2019.ssbd03.utils.roles.MokRoles;

import javax.annotation.security.RolesAllowed;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet odpowiedzialny za wylogowanie uzytkownika. Metoda doGet unieważania obecną sesję i przekierowuje do
 * strony głównej.
 */
@WebServlet(name = "LogoutServlet", urlPatterns = {"/logout"})
@RolesAllowed(MokRoles.LOGOUT)
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getSession().invalidate();
        resp.sendRedirect(req.getContextPath());
    }
}
