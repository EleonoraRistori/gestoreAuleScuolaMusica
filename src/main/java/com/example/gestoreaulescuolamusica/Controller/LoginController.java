package com.example.gestoreaulescuolamusica.Controller;

import java.sql.SQLException;
import com.example.gestoreaulescuolamusica.Model.UserManager;

public class LoginController {
    UserManager userManager;


    public LoginController(UserManager userManager) {
        this.userManager = userManager;
    }

    public boolean isAnExistingUser(String username){
        return userManager.existingUser(username);
    }

    public String getUserPassword(String username){
        return userManager.getUserPassword(username);
    }

    public boolean addUser(String username, String password, String instrument, int type) throws SQLException {
        return userManager.addUser(username, password, instrument, type);
    }

    public int getUserType(String username){
        return userManager.getUserType(username);
    }

}

