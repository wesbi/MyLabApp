package com.example.myapplication.lab4;

public class Player {
    private String name;
    private boolean present;
    private String team; // "Красные" или "Зелёные" или ""

    public Player(String name) {
        this.name = name;
        this.present = false;
        this.team = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPresent() {
        return present;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }
}


