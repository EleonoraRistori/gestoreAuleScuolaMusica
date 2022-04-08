package com.example.gestoreaulescuolamusica.Model;

public class Instrument {
    private final String name;
    private final boolean heavy;
    private final boolean noisy;

    public Instrument(String name, boolean heavy, boolean noisy) {
        this.name = name;
        this.heavy = heavy;
        this.noisy = noisy;
    }

    public String getName() {
        return name;
    }

    public boolean isHeavy() {
        return heavy;
    }

    public boolean isNoisy() {
        return noisy;
    }
}
