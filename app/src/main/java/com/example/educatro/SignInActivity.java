package com.example.educatro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.educatro.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class SignInActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button signInButton;
    private Button googleButton;
    private Button facebookButton;
    private TextView forgotPasswordTextView;
    private TextView signUpTextView;
    
    private FirebaseDatabase database;
    private DatabaseReference usersRef;
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "EducatroPrefs";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USER_EMAIL = "email";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Check if user is already logged in
        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        if (sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)) {
            // User is already logged in, go directly to MainActivity
            String userId = sharedPreferences.getString(KEY_USER_ID, "");
            if (!TextUtils.isEmpty(userId)) {
                navigateToMainActivity(userId);
                return;
            }
        }
        
        setContentView(R.layout.activity_sign_in);
        
        // Initialize Firebase
        database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("users");
        
        // Initialize UI components
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        signInButton = findViewById(R.id.signInButton);
        googleButton = findViewById(R.id.googleButton);
        facebookButton = findViewById(R.id.facebookButton);
        forgotPasswordTextView = findViewById(R.id.forgotPasswordTextView);
        signUpTextView = findViewById(R.id.signUpTextView);
        
        // Set click listeners
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        
        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Google sign-in would be implemented here
                Toast.makeText(SignInActivity.this, "Google sign-in not implemented yet", Toast.LENGTH_SHORT).show();
            }
        });
        
        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Facebook sign-in would be implemented here
                Toast.makeText(SignInActivity.this, "Facebook sign-in not implemented yet", Toast.LENGTH_SHORT).show();
            }
        });
        
        forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to forgot password screen
                Intent intent = new Intent(SignInActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
        
        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to sign up screen
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
    
    private void signIn() {
        final String email = emailEditText.getText().toString().trim();
        final String password = passwordEditText.getText().toString().trim();
        
        // Validate input
        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Email is required");
            return;
        }
        
        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Password is required");
            return;
        }
        
        // Show progress
        signInButton.setEnabled(false);
        
        // Check if user exists and password is correct
        usersRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    boolean authenticated = false;
                    User authenticatedUser = null;
                    
                    // Check all users with this email (should be only one)
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        User user = userSnapshot.getValue(User.class);
                        if (user != null && Objects.equals(user.getPassword(), password)) {
                            authenticated = true;
                            authenticatedUser = user;
                            break;
                        }
                    }
                    
                    if (authenticated && authenticatedUser != null) {
                        // Password is correct, sign in successful
                        Toast.makeText(SignInActivity.this, "Sign in successful", Toast.LENGTH_SHORT).show();
                        
                        // Save login state in SharedPreferences
                        saveLoginState(authenticatedUser.getUserId(), email);
                        
                        // Navigate to main activity
                        navigateToMainActivity(authenticatedUser.getUserId());
                    } else {
                        // Password is incorrect
                        Toast.makeText(SignInActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                        signInButton.setEnabled(true);
                    }
                } else {
                    // User not found
                    Toast.makeText(SignInActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                    signInButton.setEnabled(true);
                }
            }
            
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SignInActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                signInButton.setEnabled(true);
            }
        });
    }
    
    private void saveLoginState(String userId, String email) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER_ID, userId);
        editor.putString(KEY_USER_EMAIL, email);
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.apply();
    }
    
    private void navigateToMainActivity(String userId) {
        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
        intent.putExtra("userId", userId);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
} 