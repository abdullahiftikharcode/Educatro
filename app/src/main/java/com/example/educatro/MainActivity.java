package com.example.educatro;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.educatro.fragments.AccountFragment;
import com.example.educatro.fragments.BookmarksFragment;
import com.example.educatro.fragments.BrowseFragment;
import com.example.educatro.fragments.MyCoursesFragment;
import com.example.educatro.fragments.NotificationsFragment;
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
                    selectedFragment = BrowseFragment.newInstance(userId);
                } else if (itemId == R.id.nav_my_courses) {
                    selectedFragment = MyCoursesFragment.newInstance(userId);
                } else if (itemId == R.id.nav_notifications) {
                    selectedFragment = NotificationsFragment.newInstance(userId);
                } else if (itemId == R.id.nav_bookmarks) {
                    selectedFragment = BookmarksFragment.newInstance(userId);
                } else if (itemId == R.id.nav_account) {
                    selectedFragment = AccountFragment.newInstance(userId);
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
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, BrowseFragment.newInstance(userId))
                    .commit();
        }
    }
}