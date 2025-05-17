package com.example.educatro;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

import java.io.IOException;

public class SplashActivity extends AppCompatActivity {

    private GifImageView gifImageView;
    private GifDrawable gifDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        gifImageView = findViewById(R.id.splashGifImageView);
        
        try {
            // Get the GIF drawable from the GifImageView
            gifDrawable = (GifDrawable) gifImageView.getDrawable();
            
            // Check if the animation is not already finished
            if (!gifDrawable.isAnimationCompleted()) {
                // Set a listener to determine when the animation will finish
                gifDrawable.setLoopCount(1); // Play only once
                
                // Add a handler that checks animation status
                final Handler animationHandler = new Handler(Looper.getMainLooper());
                final Runnable checkAnimation = new Runnable() {
                    @Override
                    public void run() {
                        if (gifDrawable.isAnimationCompleted()) {
                            navigateToNextScreen();
                        } else {
                            animationHandler.postDelayed(this, 100);
                        }
                    }
                };
                
                animationHandler.post(checkAnimation);
            } else {
                // Animation already completed, navigate immediately
                navigateToNextScreen();
            }
        } catch (Exception e) {
            // If there's an error loading the GIF, use a fallback timer
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    navigateToNextScreen();
                }
            }, 3000); // 3 seconds fallback
        }
    }
    
    private void navigateToNextScreen() {
        // Check if user is already logged in
        // If not, go to SignInActivity, otherwise go to MainActivity
        Intent intent = new Intent(SplashActivity.this, SignInActivity.class);
        startActivity(intent);
        finish(); // Close this activity
    }
} 