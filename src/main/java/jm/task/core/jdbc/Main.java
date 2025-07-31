package jm.task.core.jdbc;

import jm.task.core.jdbc.dao.UserDaoFactory;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;
import jm.task.core.jdbc.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        ConnectionProvider<?> connectionProvider = ProviderFactory.createConnectionProvider();
        UserService userService = new UserServiceImpl(UserDaoFactory.getUserDao(connectionProvider));

        userService.createUsersTable();
        userService.saveUser("Maksim","Bondarev", (byte) 28);
        userService.saveUser("Oleg","Tolstyi", (byte) 25);
        userService.saveUser("Radic","Ladic", (byte) 22);
        userService.saveUser("Tolic","Bolic", (byte) 18);
        userService.getAllUsers().forEach(System.out::println);
        userService.cleanUsersTable();
        userService.dropUsersTable();
    }
}
