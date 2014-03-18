package hk.ssutt.web.publicapi.bs;

import hk.ssutt.action.Action;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by fau on 17/03/14.
 */
public class GroupGetter extends HttpServlet {
    private static Action ac;

    public void init() throws ServletException {
        ac = Action.getInstance();

    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();

        String faculty = request.getParameter("faculty");
        if (faculty == null) {
            out.println("wrong query");
        } else {
            out.println(ac.getGroupsByName(faculty));
        }


    }


    public void destroy() {
// do nothing.
    }
}
