package com.example.educatro.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AccountFragment extends Fragment {

    private ImageView profileImageView;
    private ImageButton editProfileImageButton;
    private ImageButton settingsButton;
    private ImageButton moreOptionsButton;
    private TextView nameTextView;
    private TextView emailTextView;
    private TextView followersCountTextView;
    private TextView certificatesCountTextView;
    private TextView finishedCoursesCountTextView;
    private LinearLayout addNewCardLayout;
    
    private ProgressBar loadingProgressBar;
    private TextView errorTextView;

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
        editProfileImageButton = view.findViewById(R.id.editProfileImageButton);
        settingsButton = view.findViewById(R.id.settingsButton);
        moreOptionsButton = view.findViewById(R.id.moreOptionsButton);
        nameTextView = view.findViewById(R.id.nameTextView);
        emailTextView = view.findViewById(R.id.emailTextView);
        followersCountTextView = view.findViewById(R.id.followersCountTextView);
        certificatesCountTextView = view.findViewById(R.id.certificatesCountTextView);
        finishedCoursesCountTextView = view.findViewById(R.id.finishedCoursesCountTextView);
        addNewCardLayout = view.findViewById(R.id.addNewCardLayout);
        
        // Initialize progress bar and error text view
        loadingProgressBar = view.findViewById(R.id.loadingProgressBar);
        errorTextView = view.findViewById(R.id.errorTextView);
        
        // Setup favorite categories buttons
        Button historyButton = view.findViewById(R.id.historyButton);
        Button businessButton = view.findViewById(R.id.businessButton);
        Button lawButton = view.findViewById(R.id.lawButton);
        Button politicsButton = view.findViewById(R.id.politicsButton);
        Button literatureButton = view.findViewById(R.id.literatureButton);
        Button scienceButton = view.findViewById(R.id.scienceButton);
        
        // Setup payment card views
        View mainCardLayout = view.findViewById(R.id.mainCardLayout);
        View secondCardLayout = view.findViewById(R.id.secondCardLayout);
        
        // Set click listeners for payment cards
        mainCardLayout.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Viewing Main Card Details", Toast.LENGTH_SHORT).show();
        });
        
        secondCardLayout.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Viewing Oscar's Card Details", Toast.LENGTH_SHORT).show();
        });
        
        // Set up category button click listeners
        historyButton.setOnClickListener(v -> navigateToCategory("History"));
        businessButton.setOnClickListener(v -> navigateToCategory("Business & Management"));
        lawButton.setOnClickListener(v -> navigateToCategory("Law"));
        politicsButton.setOnClickListener(v -> navigateToCategory("Politics & Society"));
        literatureButton.setOnClickListener(v -> navigateToCategory("Literature"));
        scienceButton.setOnClickListener(v -> navigateToCategory("Science"));

        // Set up click listeners
        setupClickListeners();

        // Load user data
        loadUserData();
    }

    private void setupClickListeners() {
        editProfileImageButton.setOnClickListener(v -> {
            // In a real app, you would launch image picker
            Toast.makeText(getContext(), "Edit Profile Picture", Toast.LENGTH_SHORT).show();
        });

        settingsButton.setOnClickListener(v -> {
            // Navigate to settings screen
            Intent intent = new Intent(getActivity(), com.example.educatro.SettingsActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });
        
        moreOptionsButton.setOnClickListener(v -> {
            // In a real app, you would show a popup menu
            Toast.makeText(getContext(), "More Options", Toast.LENGTH_SHORT).show();
        });
        
        addNewCardLayout.setOnClickListener(v -> {
            // For now just show a toast, but in a complete app this would navigate to a payment card screen
            Toast.makeText(getContext(), "Add New Payment Card", Toast.LENGTH_SHORT).show();
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
        // Clear login state in SharedPreferences
        if (getContext() != null) {
            SharedPreferences sharedPreferences = getContext().getSharedPreferences("EducatroPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
        }
        
        // Navigate to sign in screen
        Intent intent = new Intent(getContext(), SignInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        if (getActivity() != null) {
            getActivity().finish();
        }
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

    private void navigateToCategory(String category) {
        // Implement the logic to navigate to the category screen
        Toast.makeText(getContext(), "Navigating to " + category + " category", Toast.LENGTH_SHORT).show();
    }
} 