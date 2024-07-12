package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private TextView userName;
    private ImageView userThumbnail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        userName = findViewById(R.id.user_name);
        userThumbnail = findViewById(R.id.user_thumbnail);
        RecyclerView recentlyPlayedRecycler = findViewById(R.id.recently_played_recycler);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);


        recentlyPlayedRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        List<RecentlyPlayedItem> recentlyPlayedItems = getRecentlyPlayedItems();
        recentlyPlayedRecycler.setAdapter(new RecentlyPlayedAdapter(recentlyPlayedItems, this));




        // Load user info
        loadUserInfo();
    }

    private void loadUserInfo() {
        // Load user information and set to views
        userName.setText("John Doe");
        userThumbnail.setImageResource(R.drawable.ic_user);
    }

    private void showMenuOptions() {
        // Show menu options for logout or edit profile
        Toast.makeText(this, "Menu options", Toast.LENGTH_SHORT).show();
        // Implement your logic here
    }

    private List<RecentlyPlayedItem> getRecentlyPlayedItems() {
        // Sample data for recently played items
        List<RecentlyPlayedItem> recentlyPlayedItems = new ArrayList<>();
        recentlyPlayedItems.add(new RecentlyPlayedItem("Song 1", R.drawable.ic_music_note));
        recentlyPlayedItems.add(new RecentlyPlayedItem("Song 2", R.drawable.ic_music_note));
        recentlyPlayedItems.add(new RecentlyPlayedItem("Song 3", R.drawable.ic_music_note));
        return recentlyPlayedItems;
    }
}
