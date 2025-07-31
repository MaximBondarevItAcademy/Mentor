package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.ConnectionProvider;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Logger;

public class UserDaoHibernateImpl implements UserDao {

    private static final Logger LOGGER = Logger.getLogger(UserDaoHibernateImpl.class.getName());
    private final ConnectionProvider<Session> connectionProvider;

    public UserDaoHibernateImpl(ConnectionProvider<Session> connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    private final void consumerSession(Consumer<Session> consumer) {
        Transaction transaction = null;
        try(Session session = connectionProvider.getConnection()) {
            transaction = session.beginTransaction();
            consumer.accept(session);
            transaction.commit();
        }catch (Exception e) {
            StackTraceElement[] stackTraceElements = e.getStackTrace();
            String preMethodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            LOGGER.severe("Исключение: " + e.getClass().getSimpleName() +
                    " возникло: " + stackTraceElements[0] + "\n Вызывающий метод: " + preMethodName);
            if (transaction != null && transaction.isActive()) {
                try {
                    transaction.rollback();
                    LOGGER.info("Транзакция откачена");
                } catch (Exception rollbackEx) {
                    LOGGER.severe("Не удалось откатить транзакцию: " + rollbackEx.getMessage());
                }
            }
            throw new RuntimeException(e);
        }
    }

    private final <R> R queryFunction(Function<Session, R> function) {
        try(Session session = connectionProvider.getConnection()) {
            return function.apply(session);
        }catch (Exception e) {
            StackTraceElement[] stackTraceElements = e.getStackTrace();
            String preMethodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            LOGGER.severe("Исключение: " + e.getClass().getSimpleName() +
                    " возникло: " + stackTraceElements[0] + "\n Вызывающий метод: " + preMethodName);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createUsersTable() {
        consumerSession(session -> session
                .createNativeQuery(UserConstantsQuery.SQL_CREATE_TABLE)
                .executeUpdate());
    }

    @Override
    public void dropUsersTable() {
        consumerSession(session -> session
                .createNativeQuery(UserConstantsQuery.SQL_DROP_TABLE)
                .executeUpdate());
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        consumerSession(session -> session.save(User.builder()
                .name(name)
                .lastName(lastName)
                .age(age)
                .build()));
    }

    @Override
    public void removeUserById(long id) {
        consumerSession(session -> {
            User user = session.get(User.class, id);
            session.delete(user);
        });
    }

    @Override
    public List<User> getAllUsers() {
        return queryFunction(session -> session
                .createQuery(UserConstantsQuery.HQL_SELECT_ALL, User.class)
                .getResultList());
    }

    @Override
    public void cleanUsersTable() {
        consumerSession(session -> session
                .createQuery(UserConstantsQuery.HQL_CLEAN_ALL_USERS)
                .executeUpdate());
    }
}
