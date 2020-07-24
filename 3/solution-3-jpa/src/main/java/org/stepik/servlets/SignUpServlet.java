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

public class SignUpServlet extends HttpServlet {
    private final AccountService accountService;
    private final Logger logger = LoggerFactory.getLogger(SignUpServlet.class);

    public SignUpServlet(AccountService accountService) {
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

        try {
            accountService.signUp(new User(login, password));
        } catch (AccountException e) {
            logger.error("servlet signUp exception login : {}, password : {}", login, password);
        }
        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
