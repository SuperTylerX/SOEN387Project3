package model1;

import config.AppConfig;
import model.UserManager;


import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class UserManagerFactory {
    public static UserManager userManager = null;
//    public static String ConfigFile = UserManagerFactory.class.getClassLoader().getResource("Users.json").getPath().replace("%20", " ");

    public static UserManager getInstance() {
        try {
            if (userManager == null) {
                Class UserManagerClass = Class.forName(AppConfig.getInstance().USERMANAGER_CLASS);
                Constructor UserManagerConstructor = UserManagerClass.getDeclaredConstructor(String.class);
                System.out.println(UserManagerFactory.class.getClassLoader().getResource("."));
                return (UserManager) UserManagerConstructor.newInstance(UserManagerFactory.class.getClassLoader().getResource("Users.json").getPath().replace("%20", " "));
            } else {
                return userManager;
            }
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            System.exit(-1);
            return null;
        }
    }

    public UserManagerFactory() {
    }
}
