package com.example.educatro;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText emailEditText;
    private Button sendButton;
    private ImageButton backButton;
    private FirebaseDatabase database;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // Initialize Firebase
        database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("users");

        // Initialize UI components
        emailEditText = findViewById(R.id.emailEditText);
        sendButton = findViewById(R.id.sendButton);
        backButton = findViewById(R.id.backButton);

        // Set click listeners
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Go back to previous screen
            }
        });
    }

    private void resetPassword() {
        String email = emailEditText.getText().toString().trim();

        // Validate input
        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Email is required");
            return;
        }

        // In a real app, you would use Firebase Authentication to send a password reset email
        // For this demo, we'll just show a toast message
        Toast.makeText(this, "Password reset instructions sent to " + email, Toast.LENGTH_SHORT).show();
        
        // Go back to sign in screen
        finish();
    }
} 