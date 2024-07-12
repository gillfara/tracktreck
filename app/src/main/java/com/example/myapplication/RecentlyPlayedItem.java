package com.example.myapplication;

public class RecentlyPlayedItem {
    private String songTitle;
    private int songThumbnail;

    public RecentlyPlayedItem(String songTitle, int songThumbnail) {
        this.songTitle = songTitle;
        this.songThumbnail = songThumbnail;
    }

    public String getSongTitle() {
        return songTitle;
    }

    public int getSongThumbnail() {
        return songThumbnail;
    }
}
