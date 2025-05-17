package com.example.educatro;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.educatro.adapters.CourseAdapter;
import com.example.educatro.adapters.AuthorAdapter;
import com.example.educatro.models.Author;
import com.example.educatro.models.Course;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private ImageButton backButton;
    private EditText searchEditText;
    private ImageButton filterButton;
    private TabLayout tabLayout;
    private RecyclerView resultsRecyclerView;
    private LinearLayout emptyResultsView;

    private FirebaseDatabase database;
    private DatabaseReference coursesRef;
    private DatabaseReference authorsRef;

    private CourseAdapter courseAdapter;
    private AuthorAdapter authorAdapter;
    private String userId;
    private String initialQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Get user ID from intent
        userId = getIntent().getStringExtra("userId");
        initialQuery = getIntent().getStringExtra("query");

        // Initialize Firebase
        database = FirebaseDatabase.getInstance();
        coursesRef = database.getReference("courses");
        authorsRef = database.getReference("authors");

        // Initialize views
        backButton = findViewById(R.id.backButton);
        searchEditText = findViewById(R.id.searchEditText);
        filterButton = findViewById(R.id.filterButton);
        tabLayout = findViewById(R.id.tabLayout);
        emptyResultsView = findViewById(R.id.emptyResultsView);
        
        // Set up RecyclerView - we'll add this dynamically when needed
        setupRecyclerView();
        
        // Set initial query if provided
        if (initialQuery != null && !initialQuery.isEmpty()) {
            searchEditText.setText(initialQuery);
            performSearch(initialQuery);
        }

        // Set up click listeners
        setupClickListeners();

        // Set up tab select listener
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Get current search query
                String query = searchEditText.getText().toString().trim();
                if (!query.isEmpty()) {
                    performSearch(query);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Not needed
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Not needed
            }
        });
    }

    private void setupClickListeners() {
        backButton.setOnClickListener(v -> finish());

        filterButton.setOnClickListener(v -> {
            Toast.makeText(this, "Filter functionality coming soon", Toast.LENGTH_SHORT).show();
        });

        searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            String query = searchEditText.getText().toString().trim();
            if (!query.isEmpty()) {
                performSearch(query);
                // Save to recent searches (you'd implement this in a real app)
                return true;
            }
            return false;
        });
    }

    private void setupRecyclerView() {
        resultsRecyclerView = new RecyclerView(this);
        resultsRecyclerView.setLayoutParams(new RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.MATCH_PARENT));
        resultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        // Add the RecyclerView to the root view
        LinearLayout rootView = findViewById(R.id.searchResultsContainer);
        if (rootView != null) {
            rootView.addView(resultsRecyclerView);
        }
        
        // Initialize adapters
        courseAdapter = new CourseAdapter(new ArrayList<>(), course -> {
            // Open course detail
            navigateToCourseDetail(course);
        });
        
        authorAdapter = new AuthorAdapter(new ArrayList<>());
    }

    private void navigateToCourseDetail(Course course) {
        // Create intent to open CourseDetailActivity
        android.content.Intent intent = new android.content.Intent(this, CourseDetailActivity.class);
        intent.putExtra("courseId", course.getCourseId());
        intent.putExtra("userId", userId);
        startActivity(intent);
    }

    private void performSearch(String query) {
        int tabPosition = tabLayout.getSelectedTabPosition();
        
        if (tabPosition == 0) {
            // Courses tab selected
            searchCourses(query);
        } else {
            // Authors tab selected
            searchAuthors(query);
        }
    }

    private void searchCourses(String query) {
        // Show loading indicator (you might want to add a progress bar for this)
        
        // Search for courses with titles containing the query (case-insensitive search would require a different approach)
        Query searchQuery = coursesRef.orderByChild("title");
        
        searchQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Course> results = new ArrayList<>();
                
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Course course = snapshot.getValue(Course.class);
                    if (course != null) {
                        // Simple client-side filtering for case-insensitive search
                        if (course.getTitle().toLowerCase().contains(query.toLowerCase()) || 
                            course.getCategory().toLowerCase().contains(query.toLowerCase()) ||
                            course.getDescription().toLowerCase().contains(query.toLowerCase())) {
                            results.add(course);
                        }
                    }
                }
                
                // Display results
                displayCourseResults(results);
            }
            
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(SearchActivity.this, "Search failed: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                
                // Hide loading indicator
                showEmptyResultsView();
            }
        });
    }

    private void searchAuthors(String query) {
        // Show loading indicator
        
        // Search for authors with names containing the query
        Query searchQuery = authorsRef.orderByChild("name");
        
        searchQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Author> results = new ArrayList<>();
                
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Author author = snapshot.getValue(Author.class);
                    if (author != null) {
                        // Simple client-side filtering
                        if (author.getName().toLowerCase().contains(query.toLowerCase()) || 
                            author.getCategory().toLowerCase().contains(query.toLowerCase()) ||
                            author.getBio().toLowerCase().contains(query.toLowerCase())) {
                            results.add(author);
                        }
                    }
                }
                
                // Display results
                displayAuthorResults(results);
            }
            
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(SearchActivity.this, "Search failed: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                
                // Hide loading indicator
                showEmptyResultsView();
            }
        });
    }

    private void displayCourseResults(List<Course> results) {
        if (results.isEmpty()) {
            showEmptyResultsView();
        } else {
            hideEmptyResultsView();
            resultsRecyclerView.setAdapter(courseAdapter);
            courseAdapter.setCourses(results);
        }
    }

    private void displayAuthorResults(List<Author> results) {
        if (results.isEmpty()) {
            showEmptyResultsView();
        } else {
            hideEmptyResultsView();
            resultsRecyclerView.setAdapter(authorAdapter);
            authorAdapter.setAuthors(results);
        }
    }

    private void showEmptyResultsView() {
        emptyResultsView.setVisibility(View.VISIBLE);
        if (resultsRecyclerView != null) {
            resultsRecyclerView.setVisibility(View.GONE);
        }
    }

    private void hideEmptyResultsView() {
        emptyResultsView.setVisibility(View.GONE);
        if (resultsRecyclerView != null) {
            resultsRecyclerView.setVisibility(View.VISIBLE);
        }
    }
} 