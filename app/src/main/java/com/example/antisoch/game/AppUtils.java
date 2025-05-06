// File: AppUtils.java
package com.example.antisoch.game;

import android.content.Context;

public class AppUtils {
    public static void playClickSound(Context context) {
        SoundManager.getInstance(context).playButtonClick();
    }
}
