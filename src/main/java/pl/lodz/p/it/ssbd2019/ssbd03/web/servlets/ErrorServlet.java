package pl.lodz.p.it.ssbd2019.ssbd03.web.servlets;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ServletContextTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;

import javax.inject.Inject;
import javax.mvc.Models;
import javax.servlet.ServletContext;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Servlet odpowiedzialny za wyświetlanie strony błędów HTTP
 */
@WebServlet(name = "ErrorServlet", urlPatterns = {"/ErrorServlet"})
public class ErrorServlet extends HttpServlet {
    @Inject
    private Models models;

    @Inject
    private ServletContext servletContext;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        this.doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String viewName = "pages/error.hbs";
        models.put("statusCode", response.getStatus());
        this.renderHandlebars(request, response, viewName);
    }

    private void renderHandlebars(HttpServletRequest request, HttpServletResponse response, String viewName) throws IOException {
        models.put("webContextPath", request.getContextPath());
        models.put("page", request.getRequestURI());
        models.put("viewName", viewName);

        TemplateLoader loader = new ServletContextTemplateLoader(servletContext);
        Handlebars handlebars = new Handlebars(loader);
        String viewContent = String.join("",
                Files.readAllLines(
                        Paths.get(getServletContext().getRealPath("/"), viewName),
                        StandardCharsets.UTF_8
                )
        );
        Template template = handlebars.compileInline(viewContent);
        template.apply(models.asMap(), response.getWriter());
    }
}
