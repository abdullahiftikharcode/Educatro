package com.example.educatro;

import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        
        // Validate input
        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Email is required");
            return;
        }
        
        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Password is required");
            return;
        }
        
        // In a real app, you would use Firebase Authentication
        // For this demo, we'll just check if the user exists in the database
        usersRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // User found, navigate to main activity
                    // In a real app, you would validate the password
                    Toast.makeText(SignInActivity.this, "Sign in successful", Toast.LENGTH_SHORT).show();
                    
                    // Get the first user (there should be only one with this email)
                    DataSnapshot userSnapshot = dataSnapshot.getChildren().iterator().next();
                    User user = userSnapshot.getValue(User.class);
                    
                    // Navigate to main activity
                    Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                    intent.putExtra("userId", user.getUserId());
                    startActivity(intent);
                    finish();
                } else {
                    // User not found
                    Toast.makeText(SignInActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                }
            }
            
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SignInActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
} 