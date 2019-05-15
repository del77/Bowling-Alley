package pl.lodz.p.it.ssbd2019.ssbd03.web;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.*;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.mvc.Models;
import javax.mvc.engine.ViewEngine;
import javax.mvc.engine.ViewEngineContext;
import javax.mvc.engine.ViewEngineException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * Implementacja klasy ViewEngine odpowiedzialna za przetwarzanie widoku .hbs / .handlebars (szablony Handlebars)
 */
@ApplicationScoped
@Slf4j
public class HandlebarsViewEngine implements ViewEngine {
    @Inject
    private ServletContext servletContext;

    /**
     * Metoda zwraca czy podany plik może być przetwarzany dla tego silnika.
     * Wspierane formaty: .hbs, .handlebars. Jeśli istnieje konieczność można w tym miejscu
     * sprecyzowac dowolny format pliku.
     *
     * @param view Pełna nazwa pliku bez katalogu
     * @return true jeśli format pliku jest wspierany, false w przeciwnym wypadku
     */
    @Override
    public boolean supports(String view) {
        return view.endsWith(".hbs") || view.endsWith(".handlebars");
    }

    /**
     * Metoda odpowiadajaca za przetwarzanie widoku dla zapytania.
     *
     * @param context Kontekst silnika widoku
     * @throws ViewEngineException w przypadku wystapienia błędu przetwarzania, tutaj odczyt pliku.
     */
    @Override
    public void processView(ViewEngineContext context) throws ViewEngineException {
        Models models = context.getModels();
        String viewName = context.getView();

        if (!viewName.startsWith("/"))
            viewName = "/" + viewName;

        try (PrintWriter writer = context.getResponse(HttpServletResponse.class).getWriter();
             InputStream resourceAsStream = servletContext.getResourceAsStream(viewName);
             InputStreamReader in = new InputStreamReader(resourceAsStream, StandardCharsets.UTF_8);
             BufferedReader bufferedReader = new BufferedReader(in)) {

            HttpServletResponse httpServletResponse = context.getResponse(HttpServletResponse.class);
            if (httpServletResponse != null) {
                httpServletResponse.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
            }
            models.put("webContextPath", context.getRequest(HttpServletRequest.class).getContextPath());
            models.put("page", context.getRequest(HttpServletRequest.class).getRequestURI());
            models.put("viewName", viewName);
            String viewContent = bufferedReader.lines().collect(Collectors.joining());
            final TemplateLoader loader = new ServletContextTemplateLoader(servletContext);
            final Handlebars handlebars = new Handlebars(loader);
            final Template template = handlebars.compileInline(viewContent);
            template.apply(models.asMap(), writer);

        } catch (IOException e) {
            throw new ViewEngineException(e);
        }
    }
}
