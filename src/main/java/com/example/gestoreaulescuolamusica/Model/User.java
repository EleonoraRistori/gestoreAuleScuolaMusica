package com.example.gestoreaulescuolamusica.Model;

public  class User {
    private String username;
    private String password;
    private Instrument instrument;
    private int type;

    public User(String username, String password, Instrument instrument, int type) {
        this.username = username;
        this.password = password;
        this.instrument = instrument;
        this.type = type;
    }

    public Instrument getInstrument() {
        return instrument;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getType() {
        return type;
    }
}

