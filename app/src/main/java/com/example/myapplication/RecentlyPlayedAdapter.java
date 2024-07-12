package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecentlyPlayedAdapter extends RecyclerView.Adapter<RecentlyPlayedAdapter.ViewHolder> {

    private List<RecentlyPlayedItem> recentlyPlayedItems;
    private Context context;

    public RecentlyPlayedAdapter(List<RecentlyPlayedItem> recentlyPlayedItems, Context context) {
        this.recentlyPlayedItems = recentlyPlayedItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recently_played, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecentlyPlayedItem item = recentlyPlayedItems.get(position);
        holder.songTitle.setText(item.getSongTitle());
        holder.songThumbnail.setImageResource(item.getSongThumbnail());
    }

    @Override
    public int getItemCount() {
        return recentlyPlayedItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView songThumbnail;
        public TextView songTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            songThumbnail = itemView.findViewById(R.id.song_thumbnail);
            songTitle = itemView.findViewById(R.id.song_title);
        }
    }
}
