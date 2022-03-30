package com.example.gestoreaulescuolamusica.Model;

public class Tag {
    private String aula;
    private String caratteristica;

    public Tag(String aula, String caratteristica) {
        this.aula = aula;
        this.caratteristica = caratteristica;
    }

    public String getAula() {
        return aula;
    }

    public String getCaratteristica() {
        return caratteristica;
    }
}
