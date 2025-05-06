package com.example.antisoch.game;

public class PlayerProfile {
    public String username;
    public String characterName;
    public int lastLevel;

    // Required for Gson
    public PlayerProfile() {}

    public PlayerProfile(String username, String characterName, int lastLevel) {
        this.username = username;
        this.characterName = characterName;
        this.lastLevel = lastLevel;
    }
}
