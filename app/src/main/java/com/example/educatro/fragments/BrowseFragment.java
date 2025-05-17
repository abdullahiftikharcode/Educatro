package com.example.educatro.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.educatro.CourseDetailActivity;
import com.example.educatro.R;
import com.example.educatro.adapters.CategoryAdapter;
import com.example.educatro.adapters.CourseAdapter;
import com.example.educatro.adapters.AuthorAdapter;
import com.example.educatro.models.Author;
import com.example.educatro.models.Course;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BrowseFragment extends Fragment {

    private RecyclerView categoriesRecyclerView;
    private RecyclerView recommendedCoursesRecyclerView;
    private RecyclerView topAuthorsRecyclerView;
    private RecyclerView newReleasesRecyclerView;
    private EditText searchEditText;
    private ImageView searchIcon;
    private ProgressBar loadingProgressBar;
    private TextView errorTextView;

    private CategoryAdapter categoryAdapter;
    private CourseAdapter recommendedCoursesAdapter;
    private AuthorAdapter topAuthorsAdapter;
    private CourseAdapter newReleasesAdapter;

    private FirebaseDatabase database;
    private DatabaseReference coursesRef;
    private DatabaseReference authorsRef;
    private DatabaseReference categoriesRef;

    private String userId;

    public BrowseFragment() {
        // Required empty public constructor
    }

    public static BrowseFragment newInstance(String userId) {
        BrowseFragment fragment = new BrowseFragment();
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
        coursesRef = database.getReference("courses");
        authorsRef = database.getReference("authors");
        categoriesRef = database.getReference("categories");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_browse, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        categoriesRecyclerView = view.findViewById(R.id.categoriesRecyclerView);
        recommendedCoursesRecyclerView = view.findViewById(R.id.recommendedCoursesRecyclerView);
        topAuthorsRecyclerView = view.findViewById(R.id.topAuthorsRecyclerView);
        newReleasesRecyclerView = view.findViewById(R.id.newReleasesRecyclerView);
        searchEditText = view.findViewById(R.id.searchEditText);
        searchIcon = view.findViewById(R.id.searchIcon);
        loadingProgressBar = view.findViewById(R.id.loadingProgressBar);
        errorTextView = view.findViewById(R.id.errorTextView);

        // Set up RecyclerViews
        setupCategoriesRecyclerView();
        setupRecommendedCoursesRecyclerView();
        setupTopAuthorsRecyclerView();
        setupNewReleasesRecyclerView();

        // Load data
        loadCategories();
        loadRecommendedCourses();
        loadTopAuthors();
        loadNewReleases();

        // Set up search
        setupSearch();
    }

    private void setupCategoriesRecyclerView() {
        categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        categoryAdapter = new CategoryAdapter(new ArrayList<>());
        categoriesRecyclerView.setAdapter(categoryAdapter);
    }

    private void setupRecommendedCoursesRecyclerView() {
        recommendedCoursesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recommendedCoursesAdapter = new CourseAdapter(new ArrayList<>(), new CourseAdapter.OnCourseClickListener() {
            @Override
            public void onCourseClick(Course course) {
                navigateToCourseDetail(course);
            }
        });
        recommendedCoursesRecyclerView.setAdapter(recommendedCoursesAdapter);
    }

    private void setupTopAuthorsRecyclerView() {
        topAuthorsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        topAuthorsAdapter = new AuthorAdapter(new ArrayList<>());
        topAuthorsRecyclerView.setAdapter(topAuthorsAdapter);
    }

    private void setupNewReleasesRecyclerView() {
        newReleasesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        newReleasesAdapter = new CourseAdapter(new ArrayList<>(), new CourseAdapter.OnCourseClickListener() {
            @Override
            public void onCourseClick(Course course) {
                navigateToCourseDetail(course);
            }
        });
        newReleasesRecyclerView.setAdapter(newReleasesAdapter);
    }

    private void setupSearch() {
        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle search
                String query = searchEditText.getText().toString().trim();
                if (!query.isEmpty()) {
                    // Navigate to search results
                    // For now, just show a toast
                    // Toast.makeText(getContext(), "Searching for: " + query, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadCategories() {
        showLoading();
        categoriesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> categories = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String category = snapshot.getValue(String.class);
                    categories.add(category);
                }
                categoryAdapter.setCategories(categories);
                hideLoading();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                showError("Error loading categories: " + databaseError.getMessage());
            }
        });
    }

    private void loadRecommendedCourses() {
        showLoading();
        coursesRef.orderByChild("rating").limitToLast(10).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Course> courses = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Course course = snapshot.getValue(Course.class);
                    courses.add(course);
                }
                recommendedCoursesAdapter.setCourses(courses);
                hideLoading();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                showError("Error loading recommended courses: " + databaseError.getMessage());
            }
        });
    }

    private void loadTopAuthors() {
        showLoading();
        authorsRef.orderByChild("followersCount").limitToLast(10).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Author> authors = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Author author = snapshot.getValue(Author.class);
                    authors.add(author);
                }
                topAuthorsAdapter.setAuthors(authors);
                hideLoading();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                showError("Error loading top authors: " + databaseError.getMessage());
            }
        });
    }

    private void loadNewReleases() {
        showLoading();
        // In a real app, you would order by timestamp
        coursesRef.limitToLast(10).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Course> courses = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Course course = snapshot.getValue(Course.class);
                    courses.add(course);
                }
                newReleasesAdapter.setCourses(courses);
                hideLoading();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                showError("Error loading new releases: " + databaseError.getMessage());
            }
        });
    }

    private void navigateToCourseDetail(Course course) {
        Intent intent = new Intent(getContext(), CourseDetailActivity.class);
        intent.putExtra("courseId", course.getCourseId());
        intent.putExtra("userId", userId);
        startActivity(intent);
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