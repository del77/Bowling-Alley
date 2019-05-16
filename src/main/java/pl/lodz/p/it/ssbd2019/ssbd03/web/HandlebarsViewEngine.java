package pl.lodz.p.it.ssbd2019.ssbd03.web;

import lombok.extern.slf4j.Slf4j;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.HandlebarsUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.mvc.Models;
import javax.mvc.engine.ViewEngine;
import javax.mvc.engine.ViewEngineContext;
import javax.mvc.engine.ViewEngineException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
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

        String realPath = servletContext.getRealPath(viewName);
        try {
            String viewContent = Files.readAllLines(Paths.get(realPath)).stream().collect(Collectors.joining());
            HttpServletResponse httpServletResponse = context.getResponse(HttpServletResponse.class);
            if (httpServletResponse != null) {
                models.put("viewName", viewName);
                httpServletResponse.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
                HandlebarsUtils
                        .servletContextLoader(servletContext)
                        .withModels(models)
                        .compile(viewContent)
                        .apply(httpServletResponse.getWriter());
            }

        } catch (IOException e) {
            throw new ViewEngineException(e);
        }
    }
}
