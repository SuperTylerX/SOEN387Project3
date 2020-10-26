package model;

public class User {

    private long userId;
    private String userName;
    private String userEmail;
    private String userPassword;

    public User(long userId, String userName, String userEmail, String userPassword) {
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
    }

    public User(User user){
        this.userId = user.userId;
        this.userName = user.userName;
        this.userEmail = user.userEmail;
        this.userPassword = user.userPassword;
    }

    public User clone(){
        return new User(this);
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}
