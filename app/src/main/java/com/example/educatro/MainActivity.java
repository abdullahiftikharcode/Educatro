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
    
    /**
     * Navigate to a specific tab in the bottom navigation
     * @param tabIndex the index of the tab to navigate to (0: Browse, 1: My Courses, 2: Bookmarks, 3: Notifications, 4: Account)
     */
    public void navigateToTab(int tabIndex) {
        MenuItem item = null;
        
        switch (tabIndex) {
            case 0:
                item = bottomNavigationView.getMenu().findItem(R.id.nav_browse);
                break;
            case 1:
                item = bottomNavigationView.getMenu().findItem(R.id.nav_my_courses);
                break;
            case 2:
                item = bottomNavigationView.getMenu().findItem(R.id.nav_bookmarks);
                break;
            case 3:
                item = bottomNavigationView.getMenu().findItem(R.id.nav_notifications);
                break;
            case 4:
                item = bottomNavigationView.getMenu().findItem(R.id.nav_account);
                break;
        }
        
        if (item != null) {
            item.setChecked(true);
            bottomNavigationView.setSelectedItemId(item.getItemId());
        }
    }
}