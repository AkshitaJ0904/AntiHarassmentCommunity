package com.example.antisoch.game;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ProfileManager {
    private static final String PREFS_NAME = "player_profiles";
    private static final String KEY_PROFILES = "profiles";
    private static final int MAX_PROFILES = 3;
    private static final Gson gson = new Gson();

    public static List<PlayerProfile> getProfiles(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(KEY_PROFILES, "[]");
        Type listType = new TypeToken<ArrayList<PlayerProfile>>() {}.getType();
        return gson.fromJson(json, listType);
    }

    public static void saveProfiles(Context context, List<PlayerProfile> profiles) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_PROFILES, gson.toJson(profiles)).apply(); // You can change to .commit() if needed
    }

    public static boolean canAddProfile(Context context) {
        return getProfiles(context).size() < MAX_PROFILES;
    }

    public static void addProfile(Context context, PlayerProfile newProfile) {
        List<PlayerProfile> profiles = getProfiles(context);
        if (profiles.size() < MAX_PROFILES) {
            profiles.add(newProfile);
            saveProfiles(context, profiles);
        }
    }

    public static void deleteProfile(Context context, int index) {
        List<PlayerProfile> profiles = getProfiles(context);
        if (index >= 0 && index < profiles.size()) {
            profiles.remove(index);
            saveProfiles(context, profiles);
        }
    }

    public static PlayerProfile getProfile(Context context, int index) {
        List<PlayerProfile> profiles = getProfiles(context);
        return (index >= 0 && index < profiles.size()) ? profiles.get(index) : null;
    }
}
