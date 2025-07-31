package jm.task.core.jdbc.dao;

public final class UserConstantsQuery {
    private UserConstantsQuery() {}

    public static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS users";
    public static final String SQL_INSERT_USER = "INSERT INTO users (name, lastName, age) VALUES (?, ?, ?)";
    public static final String SQL_DELETE_USER = "DELETE FROM users WHERE id = ?";
    public static final String SQL_SELECT_ALL = "SELECT id, name, lastName, age FROM users";
    public static final String SQL_TRUNCATE = "TRUNCATE TABLE users";
    public static final String SQL_CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS users (
                id SERIAL PRIMARY KEY,
                name VARCHAR(255),
                lastName VARCHAR(255),
                age INTEGER
            );
            """;
    public static final String HQL_SELECT_ALL = "select u from User u";
    public static final String HQL_CLEAN_ALL_USERS = "DELETE FROM User";
}
