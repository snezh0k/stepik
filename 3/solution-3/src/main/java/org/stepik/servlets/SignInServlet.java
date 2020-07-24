package org.stepik.servlets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stepik.accounts.AccountException;
import org.stepik.accounts.AccountService;
import org.stepik.accounts.datasets.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SignInServlet extends HttpServlet {
    private final Logger logger = LoggerFactory.getLogger(SignInServlet.class);
    private final AccountService accountService;

    public SignInServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");
        String password = req.getParameter("password");

        if (login == null || password == null) {
            resp.setContentType("text/html;charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        User user = null;
        try {
            user = accountService.signIn(login, password);
        } catch (AccountException e) {
            logger.error("servlet signIn exception login : {}, password : {}", login, password);
        }

        if (user == null) {
            resp.setContentType("text/html;charset=utf-8");
            resp.getWriter().println("Unauthorized");
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        resp.setContentType("text/html;charset=utf-8");
        resp.getWriter().println("Authorized: " + login);
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
