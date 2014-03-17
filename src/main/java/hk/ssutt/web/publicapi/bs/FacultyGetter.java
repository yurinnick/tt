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
        System.out.println(System.getProperty("user.dir"));
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
            throws ServletException, IOException {
// Set response content type
        response.setContentType("text/html");
// Actual logic goes here.
        String faculty = request.getParameter("faculty");
        String group = request.getParameter("grp");
        PrintWriter out = response.getWriter();
        if (faculty == null || group == null)
            out.println("null");
        else {
            String message = String.format("/usr/local/tt/timetables/%s/%s.json contents", faculty, group);
            out.println(message);
        }
    }

    public void destroy() {
// do nothing.
    }
}