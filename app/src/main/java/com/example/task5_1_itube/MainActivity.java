package com.example.task5_1_itube;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    // Declare UI elements and database helper
    private EditText usernameInput;
    private EditText passwordInput;
    private Button loginButton;
    private Button signupButton;
    private UserDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable edge-to-edge display for the activity
        EdgeToEdge.enable(this);

        // Set the layout resource to activity_main.xml
        setContentView(R.layout.activity_main);

        // Ensure proper display in full-screen mode by setting padding for system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize UI elements by finding them from the layout
        usernameInput = findViewById(R.id.usernameInput); // Username input field
        passwordInput = findViewById(R.id.passwordInput); // Password input field
        loginButton = findViewById(R.id.loginButton);     // Login button
        signupButton = findViewById(R.id.signupButton);   // Signup button

        // Initialize the database helper to manage user data
        dbHelper = new UserDatabaseHelper(this);

        // Set a click listener for the login button
        loginButton.setOnClickListener(v -> {
            // Retrieve the text entered by the user in the username and password fields
            String username = usernameInput.getText().toString();
            String password = passwordInput.getText().toString();

            // Validate the entered username and password against the database
            if (dbHelper.validateUser(username, password)) {
                // Display a success message using a toast
                Toast.makeText(MainActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();

                // Create an intent to start the MenuActivity
                Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                startActivity(intent); // Start the MenuActivity

                // Close the current activity to prevent the user from returning to the login screen
                finish();
            } else {
                // Display an error message if the username or password is invalid
                Toast.makeText(MainActivity.this, "Invalid username or password.", Toast.LENGTH_SHORT).show();
            }
        });

        // Set a click listener for the signup button
        signupButton.setOnClickListener(v -> {
            // Create an intent to start the SignUpActivity
            Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
            startActivity(intent); // Start the SignUpActivity
        });
    }
}
