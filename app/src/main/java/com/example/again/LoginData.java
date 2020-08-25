package com.example.again;

public class LoginData{
    String user_email, user_passwd;

    public LoginData(String user_email, String user_passwd) {
        this.user_email = user_email;
        this.user_passwd = user_passwd;
    }

    public String getUser_email() {
        return user_email;
    }

    public String getUser_passwd() {
        return user_passwd;
    }
}
class LoginResponse{
    private int state;
    private String message;
    private String user_email;

    public int getState() {
        return state;
    }

    public String getMessage() {
        return message;
    }

    public String getUser_email() {
        return user_email;
    }
}