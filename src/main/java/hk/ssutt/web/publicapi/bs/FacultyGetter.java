package hk.ssutt.web.publicapi.bs;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import hk.ssutt.action.Action;


/**
 * Created by fau on 17/03/14.
 */
public class FacultyGetter extends HttpServlet {
    private static Action ac;
    public void init() throws ServletException {
        ac = Action.getInstance();

    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();

        out.print(ac.getFacultiesList());
        }


    public void destroy() {
// do nothing.
    }
}