package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.util.ConnectionProvider;
import org.hibernate.Session;
import java.sql.Connection;
import java.sql.SQLException;

public final class UserDaoFactory {
    private UserDaoFactory() {}

    public static UserDao getUserDao(ConnectionProvider<?> connectionProvider) throws SQLException {
            if (connectionProvider.getConnection() instanceof Session) {
                return new UserDaoHibernateImpl((ConnectionProvider<Session>) connectionProvider);
            }else if (connectionProvider.getConnection() instanceof Connection) {
                return new UserDaoJDBCImpl((ConnectionProvider<Connection>) connectionProvider);
            }else {
                throw new SQLException("Connection is not supported");
            }
    }
}
