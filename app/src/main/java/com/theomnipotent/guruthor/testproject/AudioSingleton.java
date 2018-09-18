package com.theomnipotent.guruthor.testproject;

import android.media.MediaPlayer;

public final class AudioSingleton {

    private static MediaPlayer instance;

    public static MediaPlayer getInstance() {

        if (instance == null) {
            instance = new MediaPlayer();
        }

        return instance;

    }

}
