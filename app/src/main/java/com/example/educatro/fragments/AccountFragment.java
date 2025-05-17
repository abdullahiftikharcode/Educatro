package com.example.educatro.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.educatro.R;
import com.example.educatro.SignInActivity;
import com.example.educatro.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AccountFragment extends Fragment {

    private ImageView profileImageView;
    private TextView nameTextView;
    private TextView emailTextView;
    private TextView followersCountTextView;
    private TextView followingCountTextView;
    private TextView certificatesCountTextView;
    private TextView finishedCoursesCountTextView;
    private Button editProfileButton;
    private Button settingsButton;
    private Button signOutButton;
    private ProgressBar loadingProgressBar;
    private TextView errorTextView;

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference usersRef;

    private String userId;

    public AccountFragment() {
        // Required empty public constructor
    }

    public static AccountFragment newInstance(String userId) {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        args.putString("userId", userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getString("userId");
        }

        // Initialize Firebase
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("users");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        profileImageView = view.findViewById(R.id.profileImageView);
        nameTextView = view.findViewById(R.id.nameTextView);
        emailTextView = view.findViewById(R.id.emailTextView);
        followersCountTextView = view.findViewById(R.id.followersCountTextView);
        followingCountTextView = view.findViewById(R.id.followingCountTextView);
        certificatesCountTextView = view.findViewById(R.id.certificatesCountTextView);
        finishedCoursesCountTextView = view.findViewById(R.id.finishedCoursesCountTextView);
        editProfileButton = view.findViewById(R.id.editProfileButton);
        settingsButton = view.findViewById(R.id.settingsButton);
        signOutButton = view.findViewById(R.id.signOutButton);
        loadingProgressBar = view.findViewById(R.id.loadingProgressBar);
        errorTextView = view.findViewById(R.id.errorTextView);

        // Set up click listeners
        setupClickListeners();

        // Load user data
        loadUserData();
    }

    private void setupClickListeners() {
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // In a real app, you would navigate to the edit profile screen
                Toast.makeText(getContext(), "Edit Profile", Toast.LENGTH_SHORT).show();
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // In a real app, you would navigate to the settings screen
                Toast.makeText(getContext(), "Settings", Toast.LENGTH_SHORT).show();
            }
        });

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
    }

    private void loadUserData() {
        showLoading();

        usersRef.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    displayUserData(user);
                }
                hideLoading();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                showError("Error loading user data: " + databaseError.getMessage());
            }
        });
    }

    private void displayUserData(User user) {
        nameTextView.setText(user.getName());
        emailTextView.setText(user.getEmail());
        followersCountTextView.setText(String.valueOf(user.getFollowersCount()));
        followingCountTextView.setText(String.valueOf(user.getFollowingCount()));
        certificatesCountTextView.setText(String.valueOf(user.getCertificatesCount()));
        finishedCoursesCountTextView.setText(String.valueOf(user.getFinishedCoursesCount()));

        // Load profile image with Glide
        // In a real app, you would have a profile image URL in the User model
        Glide.with(this)
                .load(R.drawable.placeholder_profile)
                .circleCrop()
                .into(profileImageView);
    }

    private void signOut() {
        auth.signOut();
        
        // Navigate to sign in screen
        Intent intent = new Intent(getContext(), SignInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finish();
    }

    private void showLoading() {
        loadingProgressBar.setVisibility(View.VISIBLE);
        errorTextView.setVisibility(View.GONE);
    }

    private void hideLoading() {
        loadingProgressBar.setVisibility(View.GONE);
    }

    private void showError(String errorMessage) {
        loadingProgressBar.setVisibility(View.GONE);
        errorTextView.setVisibility(View.VISIBLE);
        errorTextView.setText(errorMessage);
    }
} 