package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.ConnectionProvider;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Logger;

public class UserDaoJDBCImpl implements UserDao {

    private final ConnectionProvider<Connection> connectionProvider;
    private final static Logger LOGGER = Logger.getLogger(UserDaoJDBCImpl.class.getName());

    public UserDaoJDBCImpl(ConnectionProvider<Connection> connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    private final void consumerConnection(Consumer<Connection> consumer) {
        try(Connection connection = connectionProvider.getConnection()){
            connection.setAutoCommit(false);
            consumer.accept(connection);
            connection.commit();
        }catch (SQLException e) {
            StackTraceElement[] stackTraceElements = e.getStackTrace();
            String preMethodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            LOGGER.severe("Исключение: " + e.getClass().getSimpleName() +
                    " возникло: " + stackTraceElements[0] + "\n Вызывающий метод: " + preMethodName);
            throw new RuntimeException(e);
        }
    }

    private final <R> R queryFunction(Function<Connection, R> function) {
        try(Connection connection = connectionProvider.getConnection()) {
            connection.setAutoCommit(false);
            R result = function.apply(connection);
            connection.commit();
            return result;
        }catch (SQLException e) {
            StackTraceElement[] stackTraceElements = e.getStackTrace();
            String preMethodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            LOGGER.severe("Исключение: " + e.getClass().getSimpleName() +
                    " возникло: " + stackTraceElements[0] + "\n Вызывающий метод: " + preMethodName);
            throw new RuntimeException("Ошибка при выполнении запроса", e);
        }
    }

    public void createUsersTable() {
        consumerConnection(connection -> {
            try(Statement statement = connection.createStatement()) {
                statement.executeUpdate(UserConstantsQuery.SQL_CREATE_TABLE);
            }catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void dropUsersTable() {
        consumerConnection(connection -> {
            try(Statement statement = connection.createStatement()) {
                statement.executeUpdate(UserConstantsQuery.SQL_DROP_TABLE);
            }catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void saveUser(String name, String lastName, byte age) {
        consumerConnection(connection -> {
            try(PreparedStatement preparedStatement = connection
                    .prepareStatement(UserConstantsQuery.SQL_INSERT_USER)) {
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, lastName);
                preparedStatement.setByte(3, age);
                preparedStatement.executeUpdate();
            }catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void removeUserById(long id) {
        consumerConnection(connection -> {
            try(PreparedStatement preparedStatement = connection
                    .prepareStatement(UserConstantsQuery.SQL_DELETE_USER)) {
                preparedStatement.setLong(1, id);
                preparedStatement.executeUpdate();
            }catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public List<User> getAllUsers() {
        return queryFunction(connection -> {
            try (PreparedStatement ps = connection.prepareStatement(UserConstantsQuery.SQL_SELECT_ALL);
                 ResultSet rs = ps.executeQuery()) {

                List<User> users = new ArrayList<>();
                while (rs.next()) {
                    users.add(new User(
                            rs.getLong("id"),
                            rs.getString("name"),
                            rs.getString("lastName"),
                            rs.getByte("age")
                    ));
                }
                return users;
            }catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void cleanUsersTable() {
        consumerConnection(connection -> {
            try(Statement statement = connection.createStatement()) {
                statement.executeUpdate(UserConstantsQuery.SQL_TRUNCATE);
            }catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
