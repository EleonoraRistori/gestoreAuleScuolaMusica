package com.example.gestoreaulescuolamusica.Model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import com.example.gestoreaulescuolamusica.DAO.UserDAO;

public class UserManager {
    private final ArrayList<User> users;
    private final HashMap<String, Instrument> instruments;
    private final UserDAO userDAO;

    public UserManager() throws SQLException {
        userDAO =  new UserDAO();
        users = userDAO.pullUsers();
        instruments = userDAO.pullInstruments();
    }

    public boolean isNoisyUser(String username){
        for (User user : users) {
            if (Objects.equals(user.getUsername(), username) && user.getInstrument().isNoisy()) {
                return true;
            }
        }
        return false;
    }

    public String getUserPassword(String username){
        for (User user : users){
            if(Objects.equals(user.getUsername(), username))
                return user.getPassword();
        }
        return null;
    }

    public boolean existingUser(String username){
        for (User user : users) {
            if (Objects.equals(user.getUsername(), username)) {
                return true;
            }
        }
        return false;
    }

    public boolean addUser(String username, String password, String instrument, int type) throws SQLException {
        if(!existingUser(username)) {
            User user = new User(username, password, instruments.get(instrument), type);
            users.add(user);
            userDAO.addUser(username, password, instrument, type);
            return true;
        }
        return false;
    }

    public int getUserType(String username){
        for(User user : users){
            if(Objects.equals(user.getUsername(), username))
                return user.getType();
        }
        return 0;
    }

    public User getUser(String username){
        for(User user : users){
            if(Objects.equals(user.getUsername(), username))
                return user;
        }
        return null;
    }

}

