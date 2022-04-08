package com.example.gestoreaulescuolamusica.Model;

public class Tag {
    private final String room;
    private final String feature;

    public Tag(String room, String feature) {
        this.room = room;
        this.feature = feature;
    }

    public String getRoom() {
        return room;
    }

    public String getFeature() {
        return feature;
    }
}
