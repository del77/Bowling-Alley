package pl.lodz.p.it.ssbd2019.ssbd03.web.servlets;


import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.Files;

/**
 * Servlet odpowiedzialny za mapowanie ścieżki "/static/*" dla serwowania plików statycznych.
 * mapuje katalog "static" w katalogu "webapp" na ściężke "/static" w kontekście aplikacji.
 */
@WebServlet("/static/*")
public class StaticFilesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String filename = URLDecoder.decode(request.getPathInfo().substring(1), "UTF-8");
        String path =  getServletContext().getRealPath("/static");
        File file = new File(path, filename);
        if(!file.isDirectory() && file.exists()) {
            response.setHeader("Content-Type", getServletContext().getMimeType(filename));
            response.setHeader("Content-Length", String.valueOf(file.length()));
            response.setHeader("Content-Disposition", "inline; filename=\"" + file.getName() + "\"");
            Files.copy(file.toPath(), response.getOutputStream());
        } else {
            response.setStatus(404);
        }
    }
}
