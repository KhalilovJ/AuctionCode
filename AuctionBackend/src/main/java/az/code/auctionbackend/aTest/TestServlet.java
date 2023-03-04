package az.code.auctionbackend.aTest;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "TestServlet", value = "/TestServlet")
public class TestServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");

//        String paramName = "id";
//        String paramValue = request.getParameter(paramName);
//        message = paramValue;

        String [] uri = request.getRequestURI().split("/");


        // Hello
        PrintWriter out = response.getWriter();
        out.println("<html><body>");

        if (uri[3].equals("person")){

            if (uri[4].matches("\\d+")){
                out.println("User id: " + uri[4]);
            }else {
                out.println("ID error");
            }
        }
        else {
            out.println("Page error");
        }

//        if (message != "Hello World!") {
//            out.println("<h1>" + "User id: " + message + "</h1>");
//            out.println("Current URL : " + request.getRequestURL());
//
//        } else {
//            out.println("<h1>" + "Error 404" + "</h1>");
//        }
        out.println("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
