package com.example.educatro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class SettingsActivity extends AppCompatActivity {

    private LinearLayout changePasswordLayout;
    private LinearLayout notificationsLayout;
    private LinearLayout privacySettingsLayout;
    private LinearLayout signOutLayout;
    
    private LinearLayout currencyLayout;
    private LinearLayout languagesLayout;
    private LinearLayout linkedAccountsLayout;
    
    private Switch newsletterSwitch;
    private Switch textMessagesSwitch;
    private Switch phoneCallsSwitch;
    
    private TextView currencyValueTextView;
    private TextView languageValueTextView;
    private TextView linkedAccountsValueTextView;
    
    private FirebaseAuth auth;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        
        // Get userId from intent
        userId = getIntent().getStringExtra("userId");
        
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();
        
        // Setup back button
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> onBackPressed());
        
        // Initialize views
        initializeViews();
        
        // Setup click listeners
        setupClickListeners();
        
        // Load user preferences
        loadUserPreferences();
    }
    
    private void initializeViews() {
        // Account section
        changePasswordLayout = findViewById(R.id.changePasswordLayout);
        notificationsLayout = findViewById(R.id.notificationsLayout);
        privacySettingsLayout = findViewById(R.id.privacySettingsLayout);
        signOutLayout = findViewById(R.id.signOutLayout);
        
        // More Options section
        newsletterSwitch = findViewById(R.id.newsletterSwitch);
        textMessagesSwitch = findViewById(R.id.textMessagesSwitch);
        phoneCallsSwitch = findViewById(R.id.phoneCallsSwitch);
        
        currencyLayout = findViewById(R.id.currencyLayout);
        languagesLayout = findViewById(R.id.languagesLayout);
        linkedAccountsLayout = findViewById(R.id.linkedAccountsLayout);
        
        currencyValueTextView = findViewById(R.id.currencyValueTextView);
        languageValueTextView = findViewById(R.id.languageValueTextView);
        linkedAccountsValueTextView = findViewById(R.id.linkedAccountsValueTextView);
    }
    
    private void setupClickListeners() {
        // Account section
        changePasswordLayout.setOnClickListener(v -> {
            // Navigate to change password screen
            Toast.makeText(this, "Change Password", Toast.LENGTH_SHORT).show();
        });
        
        notificationsLayout.setOnClickListener(v -> {
            // Navigate to notifications settings screen
            Toast.makeText(this, "Notifications Settings", Toast.LENGTH_SHORT).show();
        });
        
        privacySettingsLayout.setOnClickListener(v -> {
            // Navigate to privacy settings screen
            Toast.makeText(this, "Privacy Settings", Toast.LENGTH_SHORT).show();
        });
        
        signOutLayout.setOnClickListener(v -> {
            // Sign out and navigate to sign in screen
            signOut();
        });
        
        // More Options section - switches
        newsletterSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Save newsletter preference
            // In a real app, you would update the user's preferences in the database
            Toast.makeText(this, "Newsletter: " + (isChecked ? "ON" : "OFF"), Toast.LENGTH_SHORT).show();
        });
        
        textMessagesSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Save text messages preference
            Toast.makeText(this, "Text Messages: " + (isChecked ? "ON" : "OFF"), Toast.LENGTH_SHORT).show();
        });
        
        phoneCallsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Save phone calls preference
            Toast.makeText(this, "Phone Calls: " + (isChecked ? "ON" : "OFF"), Toast.LENGTH_SHORT).show();
        });
        
        // More Options section - selections
        currencyLayout.setOnClickListener(v -> {
            // Show currency selection dialog
            Toast.makeText(this, "Select Currency", Toast.LENGTH_SHORT).show();
        });
        
        languagesLayout.setOnClickListener(v -> {
            // Show language selection dialog
            Toast.makeText(this, "Select Language", Toast.LENGTH_SHORT).show();
        });
        
        linkedAccountsLayout.setOnClickListener(v -> {
            // Navigate to linked accounts screen
            Toast.makeText(this, "Linked Accounts", Toast.LENGTH_SHORT).show();
        });
    }
    
    private void loadUserPreferences() {
        // In a real app, you would load these values from the database
        // For now, set default values
        newsletterSwitch.setChecked(true);
        textMessagesSwitch.setChecked(false);
        phoneCallsSwitch.setChecked(false);
        
        currencyValueTextView.setText("$-USD");
        languageValueTextView.setText("English");
        linkedAccountsValueTextView.setText("Facebook, Google");
    }
    
    private void signOut() {
        // Sign out from Firebase
        auth.signOut();
        
        // Navigate to sign in screen
        Intent intent = new Intent(this, SignInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    
    
} 