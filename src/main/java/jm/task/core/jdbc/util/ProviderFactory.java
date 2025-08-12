package jm.task.core.jdbc.util;

public final class ProviderFactory {
    private ProviderFactory() {}

    public static ConnectionProvider<?> createConnectionProvider() {
        String providerName = PropertiesUtil.get("provider.connection");
        if(providerName.equals("hibernate")) {
            return new UtilProviderHibernateImpl();
        }else if(providerName.equals("jdbc")) {
            return new UtilProviderJdbcImpl();
        }else {
            throw new IllegalArgumentException("Unsupported provider: " + providerName);
        }
    }
}
