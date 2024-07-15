package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.media3.common.C;
import androidx.media3.common.MediaItem;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.database.DatabaseProvider;
import androidx.media3.database.StandaloneDatabaseProvider;
import androidx.media3.datasource.DataSource;
import androidx.media3.datasource.DefaultHttpDataSource;
import androidx.media3.datasource.cache.Cache;
import androidx.media3.datasource.cache.CacheDataSource;
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor;
import androidx.media3.datasource.cache.SimpleCache;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory;
import androidx.media3.ui.PlayerView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    private PlayerView playerView;
    private ExoPlayer player;
    private ListView songListView;
    private ArrayList<CustomMusicItem> songList;
    private CustomMusicAdapter songAdapter;
    private TextView currentSongTitle, currentSongArtist;
    private ImageView playPauseButton, nextButton, prevButton;

    private static final String TAG = "MainActivity";

    private void playSelectedSong(CustomMusicItem song) {
        MediaItem mediaItem = MediaItem.fromUri(song.getSongUrl());
        player.setMediaItem(mediaItem);
        player.prepare();
        player.play();

        // Update current song UI
        currentSongTitle.setText(song.getTitle());
        playPauseButton.setImageResource(R.drawable.ic_pause);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.release();
        }
    }

    @UnstableApi
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        playerView = findViewById(R.id.player_view);
        songListView = findViewById(R.id.music_list);
        currentSongTitle = findViewById(R.id.current_song_title);
        currentSongArtist = findViewById(R.id.current_song_artist);
        playPauseButton = findViewById(R.id.play_pause_button);
        nextButton = findViewById(R.id.next_button);
        prevButton = findViewById(R.id.prev_button);

        // Music player and cache setup
        File cacheDir = new File(getCacheDir(), "media");
        DatabaseProvider databaseProvider = new StandaloneDatabaseProvider(this);
        Cache cache = new SimpleCache(cacheDir, new LeastRecentlyUsedCacheEvictor(3 * 1024 * 1024), databaseProvider);
        DataSource.Factory httpDataSourceFactory = new DefaultHttpDataSource.Factory();
        DataSource.Factory cacheDataSourceFactory = new CacheDataSource.Factory().setCache(cache).setUpstreamDataSourceFactory(httpDataSourceFactory);

        songList = new ArrayList<>();

        // AsyncHttpClient to fetch songs
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://192.168.43.151/music", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject item = response.getJSONObject(i);
                        String path = item.getString("path");
                        String title = item.getString("name");
                        songList.add(new CustomMusicItem(title, "http://192.168.43.151/hls/" + path));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                // Update UI on the main thread
                runOnUiThread(() -> {
                    songAdapter.notifyDataSetChanged();
                    // Add all songs to the player playlist
                    for (CustomMusicItem song : songList) {
                        MediaItem mediaItem = MediaItem.fromUri(song.getSongUrl());
                        player.addMediaItem(mediaItem);
                    }
                    player.prepare();
                });
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                throwable.printStackTrace(); // Print stack trace for debugging
                Toast.makeText(MainActivity.this, "Failed to load songs", Toast.LENGTH_SHORT).show();
            }
        });

        // Set up the song list view
        songAdapter = new CustomMusicAdapter(this, R.layout.list_item_music, songList);
        songListView.setAdapter(songAdapter);

        songListView.setOnItemClickListener((parent, view, position, id) -> {
            // Handle song selection
            CustomMusicItem selectedSong = songList.get(position);
            playSelectedSong(selectedSong);
        });

        player = new ExoPlayer.Builder(this)
                .setMediaSourceFactory(new DefaultMediaSourceFactory(this).setDataSourceFactory(cacheDataSourceFactory))
                .build();
        playerView.setPlayer(player);

        // Handle play/pause button
        playPauseButton.setOnClickListener(v -> {
            if (player.isPlaying()) {
                player.pause();
                playPauseButton.setImageResource(R.drawable.ic_play);
            } else {
                player.play();
                playPauseButton.setImageResource(R.drawable.ic_pause);
            }
        });

        // Handle next button
        nextButton.setOnClickListener(v -> {
            Log.d(TAG, "Next button clicked");
            player.seekToNextMediaItem();
            if (player.hasNextMediaItem()) {
                player.play();
            } else {
                Toast.makeText(MainActivity.this, "No next song", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle previous button
        prevButton.setOnClickListener(v -> {
            Log.d(TAG, "Previous button clicked");
            player.seekToPreviousMediaItem();
            if (player.hasPreviousMediaItem()) {
                player.play();
            } else {
                Toast.makeText(MainActivity.this, "No previous song", Toast.LENGTH_SHORT).show();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
