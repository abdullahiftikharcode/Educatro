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

import java.util.ArrayList;
import java.util.List;

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
                // Check if user exists
                if (!dataSnapshot.exists()) {
                    showError("User not found");
                    return;
                }
                
                try {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        displayUserData(user);
                        
                        // Check if user has favorite categories, if not, add some dummy categories
                        if (user.getFavoriteCategories() == null || user.getFavoriteCategories().isEmpty()) {
                            addDummyCategories();
                        }
                    }
                } catch (Exception e) {
                    // Handle the HashMap to List conversion issue
                    // If we get here, it means there was an error deserializing the user object
                    // We'll create a new User object with basic info and add dummy categories
                    
                    String name = dataSnapshot.child("name").getValue(String.class);
                    String email = dataSnapshot.child("email").getValue(String.class);
                    
                    User user = new User();
                    user.setUserId(userId);
                    if (name != null) user.setName(name);
                    if (email != null) user.setEmail(email);
                    
                    // Try to get other user data
                    Long followersCount = dataSnapshot.child("followersCount").getValue(Long.class);
                    Long followingCount = dataSnapshot.child("followingCount").getValue(Long.class);
                    Long certificatesCount = dataSnapshot.child("certificatesCount").getValue(Long.class);
                    Long finishedCoursesCount = dataSnapshot.child("finishedCoursesCount").getValue(Long.class);
                    
                    if (followersCount != null) user.setFollowersCount(followersCount.intValue());
                    if (followingCount != null) user.setFollowingCount(followingCount.intValue());
                    if (certificatesCount != null) user.setCertificatesCount(certificatesCount.intValue());
                    if (finishedCoursesCount != null) user.setFinishedCoursesCount(finishedCoursesCount.intValue());
                    
                    displayUserData(user);
                    
                    // Check if there are any favorite categories
                    if (!dataSnapshot.hasChild("favoriteCategories") || !dataSnapshot.child("favoriteCategories").hasChildren()) {
                        addDummyCategories();
                    } else {
                        // There are categories but in HashMap format - we don't need to do anything
                        // The UI will be updated based on the buttons' click listeners
                    }
                }
                
                hideLoading();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                showError("Error loading user data: " + databaseError.getMessage());
            }
        });
    }
    
    private void addDummyCategories() {
        List<String> dummyCategories = new ArrayList<>();
        dummyCategories.add("History");
        dummyCategories.add("Business & Management");
        dummyCategories.add("Law");
        dummyCategories.add("Politics & Society");
        dummyCategories.add("Literature");
        dummyCategories.add("Science");
        
        // First clear any existing categories then add new ones
        usersRef.child(userId).child("favoriteCategories").removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Now add the dummy categories as a list with indices
                for (int i = 0; i < dummyCategories.size(); i++) {
                    usersRef.child(userId).child("favoriteCategories").child(String.valueOf(i)).setValue(dummyCategories.get(i));
                }
            }
        });
        
        // Update UI to reflect the dummy categories
        updateCategoryButtonsUI("History"); // Default select the first category
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
        // Update buttons UI to show selected category
        updateCategoryButtonsUI(category);
        
        // Show toast with the selected category
        Toast.makeText(getContext(), "Selected category: " + category, Toast.LENGTH_SHORT).show();
        
        // In a full implementation, we would navigate to the category screen
        // or filter courses by this category
    }
    
    private void updateCategoryButtonsUI(String selectedCategory) {
        // Reset all buttons to default style
        Button historyButton = getView().findViewById(R.id.historyButton);
        Button businessButton = getView().findViewById(R.id.businessButton);
        Button lawButton = getView().findViewById(R.id.lawButton);
        Button politicsButton = getView().findViewById(R.id.politicsButton);
        Button literatureButton = getView().findViewById(R.id.literatureButton);
        Button scienceButton = getView().findViewById(R.id.scienceButton);
        
        // Set all buttons to default color (gray)
        historyButton.setTextColor(getResources().getColor(R.color.gray_light));
        businessButton.setTextColor(getResources().getColor(R.color.gray_light));
        lawButton.setTextColor(getResources().getColor(R.color.gray_light));
        politicsButton.setTextColor(getResources().getColor(R.color.gray_light));
        literatureButton.setTextColor(getResources().getColor(R.color.gray_light));
        scienceButton.setTextColor(getResources().getColor(R.color.gray_light));
        
        // Set the selected button to primary color
        switch (selectedCategory) {
            case "History":
                historyButton.setTextColor(getResources().getColor(R.color.primary));
                break;
            case "Business & Management":
                businessButton.setTextColor(getResources().getColor(R.color.primary));
                break;
            case "Law":
                lawButton.setTextColor(getResources().getColor(R.color.primary));
                break;
            case "Politics & Society":
                politicsButton.setTextColor(getResources().getColor(R.color.primary));
                break;
            case "Literature":
                literatureButton.setTextColor(getResources().getColor(R.color.primary));
                break;
            case "Science":
                scienceButton.setTextColor(getResources().getColor(R.color.primary));
                break;
        }
    }
} 