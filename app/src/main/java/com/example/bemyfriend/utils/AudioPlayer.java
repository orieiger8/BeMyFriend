package com.example.bemyfriend.utils;

import android.content.Context;
import android.media.MediaPlayer;

public class AudioPlayer {
    private MediaPlayer mp;

    private static AudioPlayer _instance;

    public static AudioPlayer getInstance() {
        if (_instance == null) {
            _instance = new AudioPlayer();
        }

        return _instance;
    }

    private AudioPlayer() {
    }

    public void stop() {
        if (mp != null) {
            mp.release();
            mp = null;
        }
    }

    public void play(Context context, int rId) {
        stop();

        mp = MediaPlayer.create(context, rId);
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                stop();
            }
        });

        mp.start();
    }

    public void play(Context context, int rId, float volume) {
        stop();

        mp = MediaPlayer.create(context, rId);
        setVolume(volume);
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                stop();
            }
        });

        mp.start();
    }

    public boolean isPlaying() {
        try {
            if (mp != null) {
                return mp.isPlaying();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public void setVolume(float volume) {
        if (mp != null) {
            mp.setVolume(volume, volume);
        }
    }
}
