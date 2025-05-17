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

import java.util.UUID;

public class SignUpActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button signUpButton;
    private Button googleButton;
    private Button facebookButton;
    private TextView signInTextView;
    
    private FirebaseDatabase database;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        
        // Initialize Firebase
        database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("users");
        
        // Initialize UI components
        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        signUpButton = findViewById(R.id.signUpButton);
        googleButton = findViewById(R.id.googleButton);
        facebookButton = findViewById(R.id.facebookButton);
        signInTextView = findViewById(R.id.signInTextView);
        
        // Set click listeners
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
        
        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Google sign-up would be implemented here
                Toast.makeText(SignUpActivity.this, "Google sign-up not implemented yet", Toast.LENGTH_SHORT).show();
            }
        });
        
        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Facebook sign-up would be implemented here
                Toast.makeText(SignUpActivity.this, "Facebook sign-up not implemented yet", Toast.LENGTH_SHORT).show();
            }
        });
        
        signInTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go back to sign in screen
                finish();
            }
        });
    }
    
    private void signUp() {
        final String name = nameEditText.getText().toString().trim();
        final String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        
        // Validate input
        if (TextUtils.isEmpty(name)) {
            nameEditText.setError("Name is required");
            return;
        }
        
        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Email is required");
            return;
        }
        
        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Password is required");
            return;
        }
        
        if (password.length() < 6) {
            passwordEditText.setError("Password must be at least 6 characters");
            return;
        }
        
        // Check if email is already registered
        usersRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Email already registered
                    Toast.makeText(SignUpActivity.this, "Email already registered", Toast.LENGTH_SHORT).show();
                } else {
                    // Create new user
                    String userId = UUID.randomUUID().toString();
                    User user = new User(userId, name, email);
                    
                    usersRef.child(userId).setValue(user.toMap()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(SignUpActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                
                                // Navigate to main activity
                                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                intent.putExtra("userId", userId);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(SignUpActivity.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
            
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SignUpActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
} 