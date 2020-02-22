package com.vahapps.faunacare;

import android.app.Application;

/**
 * Created by Vaibhavi on 08-Mar-18.
 */

public class UserType extends Application {
    private String userType;

    public UserType(String userType, String userName) {
        this.userType = userType;
        this.userName = userName;
    }

    public UserType() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    private String userName;

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
