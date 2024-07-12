package com.example.myapplication;

public class CustomMusicItem {
    private String albumCoverUrl;
    private String title;
    private String artist;
    private String songUrl;

    public CustomMusicItem(String albumCoverUrl, String title, String artist, String songUrl) {
        this.albumCoverUrl = albumCoverUrl;
        this.title = title;
        this.artist = artist;
        this.songUrl = songUrl;
    }

    public String getAlbumCoverUrl() {
        return albumCoverUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getSongUrl() {
        return songUrl;
    }
}
