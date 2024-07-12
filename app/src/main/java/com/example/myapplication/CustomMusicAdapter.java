package com.example.myapplication;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class CustomMusicAdapter extends ArrayAdapter<CustomMusicItem> {

    private Context context;
    private int resource;
    private List<CustomMusicItem> songs;

    public CustomMusicAdapter(Context context, int resource, List<CustomMusicItem> songs) {
        super(context, resource, songs);
        this.context = context;
        this.resource = resource;
        this.songs = songs;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, parent, false);
        }

        CustomMusicItem song = songs.get(position);

        ImageView albumCover = convertView.findViewById(R.id.album_cover);
        TextView songTitle = convertView.findViewById(R.id.song_title);
        TextView songArtist = convertView.findViewById(R.id.song_artist);

        Glide.with(context).load(song.getAlbumCoverUrl()).into(albumCover);
        songTitle.setText(song.getTitle());
        songArtist.setText(song.getArtist());

        return convertView;
    }
}
