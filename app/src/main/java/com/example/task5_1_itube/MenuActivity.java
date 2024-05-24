package com.example.task5_1_itube;

import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MenuActivity extends AppCompatActivity {

    // Declare UI elements
    private EditText youtubeLinkEditText; // Input field for YouTube link
    private Button playButton; // Button to play the video
    private Button addToPlaylistButton; // Button to add the video to the playlist
    private Button myPlaylistButton; // Button to view the playlist
    private UserDatabaseHelper dbHelper; // Database helper instance to interact with user data
    private final String youTubeUrlRegEx = "^(https?)?(://)?(www.)?(m.)?((youtube.com)|(youtu.be))/"; // Regular expression to match YouTube URLs

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu); // Set the layout for this activity

        // Initialize UI elements
        youtubeLinkEditText = findViewById(R.id.linkTextView); // Find the EditText for YouTube link input
        playButton = findViewById(R.id.playButton); // Find the play button
        addToPlaylistButton = findViewById(R.id.addToPlaylistButton); // Find the add to playlist button
        myPlaylistButton = findViewById(R.id.myPlaylistButton); // Find the my playlist button

        // Initialize the database helper instance
        dbHelper = new UserDatabaseHelper(this);

        // Set the click listener for the "Add to Playlist" button
        addToPlaylistButton.setOnClickListener(v -> {
            // Retrieve the YouTube link from the input field
            String youtubeLink = youtubeLinkEditText.getText().toString().trim();

            // Check if the link is not empty
            if (!youtubeLink.isEmpty()) {
                // Add the YouTube link to the database playlist
                boolean isAdded = dbHelper.addLinkToPlaylist(youtubeLink);

                // Show a toast message indicating success or failure
                if (isAdded) {
                    Toast.makeText(this, "Added to Playlist", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Failed to add to Playlist", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Prompt the user to enter a YouTube link if the input is empty
                Toast.makeText(this, "Please enter a YouTube link", Toast.LENGTH_SHORT).show();
            }
        });

        // Set the click listener for the "Play" button
        playButton.setOnClickListener(v -> {
            // Retrieve the YouTube link from the input field
            String youtubeLink = youtubeLinkEditText.getText().toString().trim();

            // Check if the link is not empty
            if (!youtubeLink.isEmpty()) {
                // Extract the YouTube video ID from the link
                String videoId = extractVideoIdFromUrl(youtubeLink);

                // Check if a valid video ID was extracted
                if (videoId != null && !videoId.isEmpty()) {
                    // Create an intent to start the VideoPlayActivity
                    Intent intent = new Intent(MenuActivity.this, VideoPlayActivity.class);
                    // Pass the video ID to the new activity
                    intent.putExtra("VIDEO_ID", videoId);
                    // Start the VideoPlayActivity
                    startActivity(intent);
                } else {
                    // Notify the user if the YouTube link is invalid
                    Toast.makeText(MenuActivity.this, "Invalid YouTube Link", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Prompt the user to enter a YouTube link if the input is empty
                Toast.makeText(this, "Please enter a YouTube link", Toast.LENGTH_SHORT).show();
            }
        });

        // Set the click listener for the "My Playlist" button
        myPlaylistButton.setOnClickListener(v -> {
            // Create an intent to start the MyPlaylistActivity
            Intent intent = new Intent(MenuActivity.this, MyPlaylistActivity.class);
            // Start the MyPlaylistActivity
            startActivity(intent);
        });
    }

    /**
     * Extracts the video ID from a YouTube URL.
     *
     * @param url The YouTube URL.
     * @return The video ID if found, otherwise null.
     */
    private String extractVideoIdFromUrl(String url) {
        // Clean the URL by removing protocol and domain
        String cleanedUrl = youTubeLinkWithoutProtocolAndDomain(url);

        // Define patterns to match possible video ID formats in the URL
        final String[] videoIdPatterns = {
                "\\?vi?=([^&]*)", // Pattern for URLs with vi or v parameter
                "watch\\?.*v=([^&]*)", // Pattern for watch URLs
                "(?:embed|vi?)/([^/?]*)", // Pattern for embed or vi URLs
                "^([A-Za-z0-9\\-]*)" // Pattern for short URLs
        };

        // Iterate over defined patterns to find a match
        for (String pattern : videoIdPatterns) {
            Pattern compiledPattern = Pattern.compile(pattern);
            Matcher matcher = compiledPattern.matcher(cleanedUrl);

            // If a match is found, return the video ID
            if (matcher.find()) {
                return matcher.group(1);
            }
        }

        // Return null if no match is found
        return null;
    }

    /**
     * Removes the protocol and domain from a YouTube URL.
     *
     * @param url The YouTube URL.
     * @return The URL without protocol and domain.
     */
    private String youTubeLinkWithoutProtocolAndDomain(String url) {
        // Compile the regex pattern to match protocol and domain
        Pattern pattern = Pattern.compile(youTubeUrlRegEx);
        Matcher matcher = pattern.matcher(url);

        // If the pattern matches, remove the protocol and domain part
        if (matcher.find()) {
            return url.replace(matcher.group(), "");
        }

        // Return the original URL if no match is found
        return url;
    }
}
