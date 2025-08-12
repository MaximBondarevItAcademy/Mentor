package jm.task.core.jdbc.util;

import java.sql.SQLException;

public interface ConnectionProvider<T> {
     T getConnection() throws SQLException;
     void close() throws Exception;
}
