package org.stepik.accounts;

import org.h2.jdbcx.JdbcDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stepik.accounts.dao.UsersDAO;
import org.stepik.accounts.datasets.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class AccountService {
    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);
    private final Connection connection;

    public AccountService() {
        this.connection = getH2Connection();
    }

    public User signIn(String login, String password) throws AccountException {
        try {
            return new UsersDAO(connection).selectUser(login, password);
        } catch (SQLException e) {
            AccountException accountException = new AccountException(e);
            logger.error("Sign in exception with login : {}, password : {} ", login, password, accountException);
            throw accountException;
        }
    }

    public long signUp(User user) throws AccountException {
        try {
            connection.setAutoCommit(false);
            UsersDAO dao = new UsersDAO(connection);
            dao.createTable();
            dao.insertUser(user);
            connection.commit();
            return dao.selectUser(user.getLogin(), user.getPassword()).getId();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ignore) {
                logger.error("signUp rollback error", ignore);
            }
            throw new AccountException(e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ignore) {
                logger.error("signUp setAutoCommit(true) error", ignore);
            }
        }
    }

    public static Connection getH2Connection() {
        try {
            String url = "jdbc:h2:./h2db";
            String name = "test";
            String pass = "test";

            JdbcDataSource ds = new JdbcDataSource();
            ds.setURL(url);
            ds.setUser(name);
            ds.setPassword(pass);

            Connection connection = DriverManager.getConnection(url, name, pass);
            return connection;
        } catch (SQLException e) {
            logger.error("H2Connection problem.", new AccountException(e));
        }
        return null;
    }


}
