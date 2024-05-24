package com.example.task5_1_itube;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class VideoPlayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the content view to the videoplayer layout
        setContentView(R.layout.videoplayer);

        // Find the YouTube player view in the layout
        YouTubePlayerView youTubePlayerView = findViewById(R.id.videoPlayer);

        // Get the video ID from the Intent extras
        String videoId = getIntent().getStringExtra("VIDEO_ID");

        // Check if the video ID is valid
        if (videoId == null || videoId.isEmpty()) {
            // If the video ID is invalid or not provided, show a toast message
            Toast.makeText(this, "Invalid video ID", Toast.LENGTH_SHORT).show();
            // Exit the method to avoid further execution
            return;
        }

        // Add a listener to the YouTube player view
        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(YouTubePlayer youTubePlayer) {
                // When the YouTube player is ready, load the video with the specified ID from the beginning
                youTubePlayer.loadVideo(videoId, 0);
            }
        });
    }
}
