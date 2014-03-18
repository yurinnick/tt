package hk.ssutt.web.admin;

import hk.ssutt.deploy.DeploySSUTT;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


/**
 * Created by fau on 17/03/14.
 */
public class Initializer implements ServletContextListener{
    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        //Notification that the servlet context is about to be shut down.
    }

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        // do all the tasks that you need to perform just after the server starts
         DeploySSUTT d = DeploySSUTT.getInstance();
        d.deploy();
        System.out.println("hello");
        //Notification that the web application initialization process is starting
    }


}

