package pl.lodz.p.it.ssbd2019.ssbd03.web.servlets;

import pl.lodz.p.it.ssbd2019.ssbd03.utils.configuration.i18n.context.LanguageContext;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.configuration.i18n.context.LocaleConfig;

import javax.inject.Inject;
import javax.mvc.Models;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet odpowiedzialny za zmianę języka, pobiera string po '/lang/' i sprawdza w kontekście
 * jezyków, czy dany jezyk istnieje, jeśli tak, to ustawia go jako obecny i wczytuje ponownie stronę bez utraty stanu.
 */
@WebServlet("/lang/*")
public class LanguageChangeServlet extends HttpServlet {

    @Inject
    private LanguageContext languageContext;

    @Inject
    private Models models;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String requestURI = req.getRequestURI();
        String langPath = requestURI.substring( requestURI.lastIndexOf("/") + 1 );
        LocaleConfig newLocaleConfig = null;
        for (LocaleConfig localeConfig : languageContext.getAllLocaleConfig()) {
            if (langPath.equalsIgnoreCase(localeConfig.locale().getLanguage())) {
                newLocaleConfig = localeConfig;
                break;
            }
        }
        if (newLocaleConfig == null) {
            newLocaleConfig = languageContext.getDefault();
        }
        languageContext.setCurrent(newLocaleConfig);
        resp.sendRedirect(models.get("path", String.class));
    }
}
