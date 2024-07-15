package com.example.myapplication;

public class CustomMusicItem {
    private String title;

    private String songUrl;

    public CustomMusicItem( String title,  String songUrl) {

        this.title = title;
        this.songUrl = songUrl;
    }


    public String getTitle() {
        return title;
    }


    public String getSongUrl() {
        return songUrl;
    }
}
