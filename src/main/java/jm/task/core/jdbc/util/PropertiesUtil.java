package jm.task.core.jdbc.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

public final class PropertiesUtil {

    private static final Properties PROPERTIES = new Properties();
    private static final Logger LOGGER = Logger.getLogger(PropertiesUtil.class.getName());

    private PropertiesUtil() {}

    static {
        loadProperties();
    }

    private static void loadProperties() {
        try(InputStream inputStream =  PropertiesUtil.class
                .getResourceAsStream("/application.properties")) {
            if (inputStream == null) {
                throw new FileNotFoundException("Файл application.properties не найден в classpath!");
            }
            PROPERTIES.load(inputStream);
        }catch (IOException e) {
            LOGGER.severe("Исключение в ProviderUtil!");
            throw new RuntimeException(e);
        }
    }

    public static String get(String key) {
        return PROPERTIES.getProperty(key);
    }
}
