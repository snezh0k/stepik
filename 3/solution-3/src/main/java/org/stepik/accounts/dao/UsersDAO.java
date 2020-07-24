package org.stepik.accounts.dao;

import org.stepik.accounts.datasets.User;
import org.stepik.executor.Executor;

import java.sql.Connection;
import java.sql.SQLException;

public class UsersDAO {
    private Executor executor;

    public UsersDAO(Connection connection) {
        this.executor = new Executor(connection);
    }

    public void insertUser(User user) throws SQLException {
        executor.execPreparedUpdate("insert into users (login, password) values (?, ?)",
                new Object[]{user.getLogin(), user.getPassword()});
    }

    public User selectUser(String login, String password) throws SQLException{
        return executor.execPreparedQuery("select * from users where login = ? and password = ?",
                new Object[]{login, password},
                resultSet -> {
                    if (resultSet.next())
                        return new User(
                                resultSet.getLong("id"),
                                resultSet.getString("login"),
                                resultSet.getString("password"));
                    return null;
                });
    }

    public void createTable() throws SQLException {
        executor.execUpdate("create table if not exists users (id bigint auto_increment, login varchar(256), password varchar(256), primary key (id))");
    }

    public void dropTable() throws SQLException {
        executor.execUpdate("drop table users");
    }
}
