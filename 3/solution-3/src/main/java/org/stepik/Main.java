package org.stepik;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stepik.accounts.AccountService;
import org.stepik.accounts.datasets.User;
import org.stepik.servlets.SignInServlet;
import org.stepik.servlets.SignUpServlet;


public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        AccountService accountService = new AccountService();
        accountService.signUp(new User("alex", "alex"));

        ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        contextHandler.addServlet(new ServletHolder(new SignInServlet(accountService)), "/signin");
        contextHandler.addServlet(new ServletHolder(new SignUpServlet(accountService)), "/signup");

        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setResourceBase("public_html");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resourceHandler, contextHandler});

        Server server = new Server(8081);
        server.setHandler(handlers);

        server.start();

        logger.info("Server started");

        server.join();
    }
}
