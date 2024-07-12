package com.example.myapplication;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
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

import java.io.File;

public class MusicActivity extends AppCompatActivity {

    private ImageView songThumbnail;
    private TextView songTitle;
    private TextView songArtist;
    private ImageView playPauseButton;
    private ImageView forwardButton;
    private ImageView rewindButton;

    @UnstableApi
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        songThumbnail = findViewById(R.id.song_thumbnail);
        songTitle = findViewById(R.id.song_title);
        songArtist = findViewById(R.id.song_artist);
        playPauseButton = findViewById(R.id.play_pause_button);
        forwardButton = findViewById(R.id.forward_button);
        rewindButton = findViewById(R.id.rewind_button);

        // Load song information
        loadSongInfo();

        // Setup media player and cache
        File cacheDir = new File(getCacheDir(), "media");
        DatabaseProvider databaseProvider = new StandaloneDatabaseProvider(this);
        Cache cache = new SimpleCache(cacheDir, new LeastRecentlyUsedCacheEvictor(3 * 1024 * 1024), databaseProvider);
        DataSource.Factory httpDataSourceFactory = new DefaultHttpDataSource.Factory();
        DataSource.Factory cacheDataSourceFactory =
                new CacheDataSource.Factory()
                        .setCache(cache)
                        .setUpstreamDataSourceFactory(httpDataSourceFactory);

        PlayerView playerView = findViewById(R.id.player_view);
        MediaItem mediaItem = MediaItem.fromUri("http://192.168.43.151/hls/index.m3u8");
        MediaItem item2 = MediaItem.fromUri("http://192.168.43.151/hls/fade.m3u8");
        ExoPlayer player = new ExoPlayer.Builder(this)
                .setMediaSourceFactory(new DefaultMediaSourceFactory(this)
                        .setDataSourceFactory(cacheDataSourceFactory))
                .build();
        playerView.setPlayer(player);
        player.setMediaItem(mediaItem);
        player.addMediaItem(item2);
        player.prepare();

        // Handle window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void loadSongInfo() {
        // Example data, replace with actual data
        songTitle.setText("Song Title");
        songArtist.setText("Artist Name");
        songThumbnail.setImageResource(R.drawable.ic_music_note);
    }
}
