package com.example.educatro;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get user ID from intent
        userId = getIntent().getStringExtra("userId");
        if (userId == null) {
            finish(); // Close activity if no user ID
            return;
        }

        // Initialize bottom navigation
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                int itemId = item.getItemId();

                if (itemId == R.id.nav_browse) {
                    // For now, we'll use placeholder fragments
                    selectedFragment = new PlaceholderFragment("Browse");
                } else if (itemId == R.id.nav_my_courses) {
                    selectedFragment = new PlaceholderFragment("My Courses");
                } else if (itemId == R.id.nav_downloads) {
                    selectedFragment = new PlaceholderFragment("Downloads");
                } else if (itemId == R.id.nav_bookmarks) {
                    selectedFragment = new PlaceholderFragment("Bookmarks");
                } else if (itemId == R.id.nav_account) {
                    selectedFragment = new PlaceholderFragment("Account");
                }

                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, selectedFragment)
                            .commit();
                    return true;
                }
                return false;
            }
        });

        // Set default fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new PlaceholderFragment("Browse"))
                .commit();
    }

    // Placeholder fragment for demonstration
    public static class PlaceholderFragment extends Fragment {
        private String title;

        public PlaceholderFragment() {
            // Required empty constructor
        }

        public PlaceholderFragment(String title) {
            this.title = title;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // We'll implement the actual fragments later
        }
    }
}