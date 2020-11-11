package model;

import config.AppConfig;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import utils.Encrypt;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class UserManager {

    public static final String CONFIG_FILE = UserManager.class.getClassLoader().getResource("../../WEB-INF/Users.json").getPath().replace("%20", " ");

    private ArrayList<User> userList;

    private static final UserManager userManager = new UserManager();

    private UserManager() {
        userList = new ArrayList<>();
        this.loadUserList();
    }

    public static UserManager getInstance() {
        return userManager;
    }

    public void loadUserList() {

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = null;

        try {
            jsonObject = (JSONObject) jsonParser.parse(new FileReader(CONFIG_FILE));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        JSONArray list = (JSONArray) jsonObject.get("userList");
        for (Object o : list) {
            Map user = (Map) o;
            String username = (String) user.get("userName");
            String password = (String) user.get("userPassword");
            String email = (String) user.get("userEmail");
            long userId = (long) user.get("userId");
            userList.add(new User(userId, username, email, password));
        }
    }

    public User authUser(String username, String password) {

        String encryptPassword = Encrypt.sha256EncryptSalt(password, AppConfig.getInstance().PASSWORD_SALT);
        for (User user : userList) {
            if (user.getUserName().equals(username) && user.getUserPassword().equals(encryptPassword)) {
                return user.clone();
            }
        }
        return null;
    }

    public String getUserNameById(long id) {
        for (User user : userList) {
            if (id == user.getUserId()) {
                return user.getUserName();
            }
        }
        return null;
    }

    public long getUserIdByName(String userName) {
        for (User user : userList) {
            if (userName.equals(user.getUserName())) {
                return user.getUserId();
            }
        }
        return -1;
    }
}
