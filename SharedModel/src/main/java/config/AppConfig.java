package config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppConfig {

    public String PASSWORD_SALT;
    public String JDBC_Driver;
    public String DB_URL;
    public String DB_NAME;
    public String DB_USER;
    public String DB_PASSWORD;
    public String POST_NUM;
    public String USERMANAGER_CLASS;

    private static AppConfig config;

    private AppConfig() {
        this.init();
    }

    public static AppConfig getInstance() {
        if (config == null) {
            config = new AppConfig();
        }
        return config;
    }

    private void init() {

        String path = AppConfig.class.getClassLoader().getResource("app.properties").getPath().replace("%20", " ");
        try (InputStream input = new FileInputStream(path)) {
            Properties prop = new Properties();
            prop.load(input);
            PASSWORD_SALT = prop.getProperty("PASSWORD_SALT");
            JDBC_Driver = prop.getProperty("JDBC_Driver");
            DB_URL = prop.getProperty("DB_URL");
            DB_NAME = prop.getProperty("DB_NAME");
            DB_USER = prop.getProperty("DB_USER");
            DB_PASSWORD = prop.getProperty("DB_PASSWORD");
            POST_NUM= prop.getProperty("POST_NUM");
            USERMANAGER_CLASS = prop.getProperty("USERMANAGER_CLASS");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
