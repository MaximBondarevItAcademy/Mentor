package jm.task.core.jdbc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public final class UtilProviderJdbcImpl implements ConnectionProvider<Connection> {

    private static final Logger LOGGER = Logger.getLogger(UtilProviderJdbcImpl.class.getName());

    private Connection connection;

    public UtilProviderJdbcImpl() {}

    @Override
    public void close() throws SQLException {
        connection.close();
    }

    @Override
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(
                    PropertiesUtil.get("jdbc.url"),
                    PropertiesUtil.get("jdbc.username"),
                    PropertiesUtil.get("jdbc.password")
            );
            LOGGER.warning("New Connected to " + PropertiesUtil.get("jdbc.url"));
        }
        return connection;
    }
}
