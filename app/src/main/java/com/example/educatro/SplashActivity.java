package com.example.educatro;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DELAY = 2000; // 2 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Handler to delay and then navigate to the next screen
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // Check if user is already logged in
            // If not, go to SignInActivity, otherwise go to MainActivity
            Intent intent = new Intent(SplashActivity.this, SignInActivity.class);
            startActivity(intent);
            finish(); // Close this activity
        }, SPLASH_DELAY);
    }
} 