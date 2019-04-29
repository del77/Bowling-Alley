package pl.lodz.p.it.ssbd2019.ssbd03.web.servlets;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ErrorServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        this.doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int statusCode = (int) request.getAttribute("javax.servlet.error.status_code");
        PrintWriter out = response.getWriter();

        response.setContentType("text/html");

        out.println("Error " + statusCode);
    }
}
