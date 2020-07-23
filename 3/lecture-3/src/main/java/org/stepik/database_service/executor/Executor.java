package org.stepik.database_service.executor;

import java.sql.*;

public class Executor {
    private final Connection connection;

    public Executor(Connection connection) {
        this.connection = connection;
    }

    public void execUpdate(String update) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(update);
        statement.close();
    }

    public <T> T execQuery(String query, ResultHandler<T> handler) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(query);
        ResultSet resultSet = statement.getResultSet();
        T value = handler.handle(resultSet);
        resultSet.close();
        statement.close();

        return value;
    }

    public <T> T execPreparedQuery(String query, Object[] objects, ResultHandler<T> handler) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(query);
        int i = 1;
        for (Object obj : objects) {
            ps.setObject(i++, obj);
        }
        ps.execute();
        ResultSet rs = ps.getResultSet();
        T value = handler.handle(rs);
        rs.close();
        ps.close();

        return value;
    }
}
