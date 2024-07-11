package com.example.myapplication;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
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

public class MainActivity extends AppCompatActivity {

    @UnstableApi
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        //music player and cache
        File caChedir = new File(getCacheDir(),"media");
        DatabaseProvider databaseProvider = new StandaloneDatabaseProvider(this);
        Cache cache = new SimpleCache(caChedir,new LeastRecentlyUsedCacheEvictor(3*1024*1024),databaseProvider);
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
//        player.setMediaItem(item2);
        player.prepare();
//        player.play();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}