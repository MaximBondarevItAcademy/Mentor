package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import java.sql.SQLException;

public final class UtilProviderHibernateImpl implements ConnectionProvider<Session> {

    private final SessionFactory sessionFactory = buildSessionFactory();

    public UtilProviderHibernateImpl() {}

    private SessionFactory buildSessionFactory() {
        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");
        configuration.addAnnotatedClass(User.class);
        return configuration.buildSessionFactory();
    }

    @Override
    public void close() throws Exception {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    @Override
    public Session getConnection() throws SQLException {
        return sessionFactory.openSession();
    }
}
