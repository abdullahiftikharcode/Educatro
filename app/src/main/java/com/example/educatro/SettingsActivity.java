package com.example.educatro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;
import android.text.InputType;
import android.widget.EditText;
import java.lang.StringBuilder;

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
            // Implementation for change password
            showChangePasswordDialog();
        });
        
        notificationsLayout.setOnClickListener(v -> {
            // Implementation for notifications settings
            showNotificationsSettingsDialog();
        });
        
        privacySettingsLayout.setOnClickListener(v -> {
            // Implementation for privacy settings
            showPrivacySettingsDialog();
        });
        
        signOutLayout.setOnClickListener(v -> {
            // Sign out and navigate to sign in screen
            signOut();
        });
        
        // More Options section - switches
        newsletterSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Save newsletter preference
            savePreference("newsletter", isChecked);
            Toast.makeText(this, "Newsletter: " + (isChecked ? "ON" : "OFF"), Toast.LENGTH_SHORT).show();
        });
        
        textMessagesSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Save text messages preference
            savePreference("textMessages", isChecked);
            Toast.makeText(this, "Text Messages: " + (isChecked ? "ON" : "OFF"), Toast.LENGTH_SHORT).show();
        });
        
        phoneCallsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Save phone calls preference
            savePreference("phoneCalls", isChecked);
            Toast.makeText(this, "Phone Calls: " + (isChecked ? "ON" : "OFF"), Toast.LENGTH_SHORT).show();
        });
        
        // More Options section - selections
        currencyLayout.setOnClickListener(v -> {
            // Show currency selection dialog
            showCurrencySelectionDialog();
        });
        
        languagesLayout.setOnClickListener(v -> {
            // Show language selection dialog
            showLanguageSelectionDialog();
        });
        
        linkedAccountsLayout.setOnClickListener(v -> {
            // Show linked accounts dialog
            showLinkedAccountsDialog();
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
        // Clear login state in SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("EducatroPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        
        // Navigate to sign in screen
        Intent intent = new Intent(this, SignInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    
    private void showChangePasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Change Password");
        
        // Set up the input
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        input.setHint("Enter new password");
        builder.setView(input);
        
        // Set up the buttons
        builder.setPositiveButton("Change", (dialog, which) -> {
            String newPassword = input.getText().toString();
            if (!newPassword.isEmpty()) {
                // In a real implementation, we would update the password in Firebase
                Toast.makeText(SettingsActivity.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        
        builder.show();
    }
    
    private void showNotificationsSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Notification Settings");
        
        String[] notificationTypes = {"App Notifications", "Email Notifications", "Push Notifications"};
        boolean[] checkedItems = {true, true, false};
        
        builder.setMultiChoiceItems(notificationTypes, checkedItems, (dialog, which, isChecked) -> {
            // Update the checked items
            checkedItems[which] = isChecked;
        });
        
        builder.setPositiveButton("Save", (dialog, which) -> {
            // In a real implementation, we would save these settings
            Toast.makeText(SettingsActivity.this, "Notification settings saved", Toast.LENGTH_SHORT).show();
        });
        
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        
        builder.show();
    }
    
    private void showPrivacySettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Privacy Settings");
        
        String[] privacySettings = {"Profile Visibility", "Activity Status", "Last Seen Status"};
        String[] options = {"Everyone", "Only Friends", "Nobody"};
        
        // This is a simplified version. In a real app, we'd use a custom layout.
        builder.setItems(privacySettings, (dialog, which) -> {
            showPrivacyOptionDialog(privacySettings[which]);
        });
        
        builder.setNegativeButton("Close", (dialog, which) -> dialog.cancel());
        
        builder.show();
    }
    
    private void showPrivacyOptionDialog(String setting) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(setting);
        
        String[] options = {"Everyone", "Only Friends", "Nobody"};
        
        builder.setItems(options, (dialog, which) -> {
            // In a real implementation, we would save this setting
            Toast.makeText(SettingsActivity.this, setting + ": " + options[which], Toast.LENGTH_SHORT).show();
        });
        
        builder.show();
    }
    
    private void showCurrencySelectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Currency");
        
        String[] currencies = {"$-USD", "€-EUR", "£-GBP", "¥-JPY", "₹-INR"};
        
        builder.setItems(currencies, (dialog, which) -> {
            currencyValueTextView.setText(currencies[which]);
            Toast.makeText(SettingsActivity.this, "Currency: " + currencies[which], Toast.LENGTH_SHORT).show();
            savePreference("currency", currencies[which]);
        });
        
        builder.show();
    }
    
    private void showLanguageSelectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Language");
        
        String[] languages = {"English", "Spanish", "French", "German", "Chinese", "Japanese"};
        
        builder.setItems(languages, (dialog, which) -> {
            languageValueTextView.setText(languages[which]);
            Toast.makeText(SettingsActivity.this, "Language: " + languages[which], Toast.LENGTH_SHORT).show();
            savePreference("language", languages[which]);
        });
        
        builder.show();
    }
    
    private void showLinkedAccountsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Linked Accounts");
        
        String[] accounts = {"Facebook", "Google", "Twitter", "Apple"};
        boolean[] checkedItems = {true, true, false, false};
        
        builder.setMultiChoiceItems(accounts, checkedItems, (dialog, which, isChecked) -> {
            // Update the checked items
            checkedItems[which] = isChecked;
        });
        
        builder.setPositiveButton("Save", (dialog, which) -> {
            // Prepare the linked accounts text
            StringBuilder linkedAccounts = new StringBuilder();
            for (int i = 0; i < checkedItems.length; i++) {
                if (checkedItems[i]) {
                    if (linkedAccounts.length() > 0) {
                        linkedAccounts.append(", ");
                    }
                    linkedAccounts.append(accounts[i]);
                }
            }
            
            linkedAccountsValueTextView.setText(linkedAccounts.toString());
            Toast.makeText(SettingsActivity.this, "Linked accounts updated", Toast.LENGTH_SHORT).show();
            savePreference("linkedAccounts", linkedAccounts.toString());
        });
        
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        
        builder.show();
    }
    
    private void savePreference(String key, boolean value) {
        SharedPreferences sharedPreferences = getSharedPreferences("EducatroPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }
    
    private void savePreference(String key, String value) {
        SharedPreferences sharedPreferences = getSharedPreferences("EducatroPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }
} 