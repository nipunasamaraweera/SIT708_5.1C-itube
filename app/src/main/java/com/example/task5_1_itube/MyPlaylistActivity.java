package com.example.task5_1_itube;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyPlaylistActivity extends AppCompatActivity {

    // RecyclerView to display the playlist
    private RecyclerView recyclerView;

    // Database helper for user data
    private UserDatabaseHelper dbHelper;

    // List to store playlist items
    private List<String> playlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the content view to the playlist layout
        setContentView(R.layout.myplaylist);

        // Initialize the database helper and RecyclerView
        dbHelper = new UserDatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerView);

        // Set up the RecyclerView
        setupRecyclerView();
    }

    /**
     * Method to set up the RecyclerView by fetching data from the database
     * and attaching an adapter with the playlist data.
     */
    private void setupRecyclerView() {
        // Initialize an empty list for the playlist
        playlist = new ArrayList<>();

        // Get playlist data from the database
        Cursor cursor = dbHelper.getPlaylist();

        // Check if the cursor is valid
        if (cursor != null) {
            // Get the index of the "link" column
            int linkColumnIndex = cursor.getColumnIndex("link");

            // If the "link" column is found, extract the playlist links
            if (linkColumnIndex >= 0) {
                while (cursor.moveToNext()) {
                    // Get the link from the cursor
                    String link = cursor.getString(linkColumnIndex);
                    // Add the link to the playlist
                    playlist.add(link);
                }
                // Close the cursor after use
                cursor.close();
            } else {
                // Show a toast message if the "link" column is not found
                Toast.makeText(this, "Column 'link' not found", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Show a toast message if the cursor is null
            Toast.makeText(this, "Failed to retrieve playlist", Toast.LENGTH_SHORT).show();
        }

        // Create an adapter with the playlist data
        PlaylistAdapter adapter = new PlaylistAdapter(playlist);

        // Set the layout manager to LinearLayoutManager for vertical scrolling
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Attach the adapter to the RecyclerView
        recyclerView.setAdapter(adapter);
    }

    /**
     * RecyclerView adapter for displaying the playlist items.
     */
    private static class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder> {

        // List of playlist links
        private final List<String> playlist;

        // Constructor to initialize the adapter with the playlist data
        PlaylistAdapter(List<String> playlist) {
            this.playlist = playlist;
        }

        @Override
        public PlaylistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // Inflate the playlist item layout
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.playlist_item, parent, false);
            // Create and return a new ViewHolder
            return new PlaylistViewHolder(view);
        }

        @Override
        public void onBindViewHolder(PlaylistViewHolder holder, int position) {
            // Get the link for the current position
            String link = playlist.get(position);
            // Set the text of the TextView to the playlist link
            holder.linkTextView.setText(link);
        }

        @Override
        public int getItemCount() {
            // Return the number of items in the playlist
            return playlist.size();
        }

        /**
         * ViewHolder class for individual playlist items.
         */
        static class PlaylistViewHolder extends RecyclerView.ViewHolder {

            // TextView to display the playlist link
            TextView linkTextView;

            // Constructor to initialize the ViewHolder
            PlaylistViewHolder(View itemView) {
                super(itemView);
                // Initialize the TextView for the link
                linkTextView = itemView.findViewById(R.id.linkTextView);
            }
        }
    }
}
