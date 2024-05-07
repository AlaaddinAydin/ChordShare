package com.example.chordshare;

import android.graphics.Bitmap;

public class MusicDetail {

    private String musicName , musciGroup, musicLyrics;

    private Bitmap musicImage , musicChord;

    public MusicDetail(String musicName, String musciGroup, String musicLyrics, Bitmap musicImage, Bitmap musicChord) {
        this.musicName = musicName;
        this.musciGroup = musciGroup;
        this.musicLyrics = musicLyrics;
        this.musicImage = musicImage;
        this.musicChord = musicChord;
    }

    public String getMusicName() {
        return musicName;
    }

    public String getMusciGroup() {
        return musciGroup;
    }

    public String getMusicLyrics() {
        return musicLyrics;
    }

    public Bitmap getMusicImage() {
        return musicImage;
    }

    public Bitmap getMusicChord() {
        return musicChord;
    }
}
