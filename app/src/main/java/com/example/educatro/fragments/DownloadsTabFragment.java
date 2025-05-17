package com.example.educatro.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.educatro.CourseDetailActivity;
import com.example.educatro.R;
import com.example.educatro.adapters.CourseAdapter;
import com.example.educatro.models.Course;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DownloadsTabFragment extends Fragment {

    private RecyclerView downloadsRecyclerView;
    private ProgressBar loadingProgressBar;
    private TextView errorTextView;
    private TextView emptyTextView;
    private Button exploreBrowseButton;

    private CourseAdapter courseAdapter;
    
    private FirebaseDatabase database;
    private DatabaseReference usersRef;
    private DatabaseReference coursesRef;
    
    private String userId;
    private List<String> downloadedCourseIds = new ArrayList<>();

    public DownloadsTabFragment() {
        // Required empty public constructor
    }

    public static DownloadsTabFragment newInstance(String userId) {
        DownloadsTabFragment fragment = new DownloadsTabFragment();
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
        coursesRef = database.getReference("courses");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_downloads_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        downloadsRecyclerView = view.findViewById(R.id.downloadsRecyclerView);
        loadingProgressBar = view.findViewById(R.id.loadingProgressBar);
        errorTextView = view.findViewById(R.id.errorTextView);
        emptyTextView = view.findViewById(R.id.emptyTextView);
        exploreBrowseButton = view.findViewById(R.id.exploreBrowseButton);

        // Set up RecyclerView
        setupRecyclerView();

        // Set up browse button click listener
        exploreBrowseButton.setOnClickListener(v -> {
            // Navigate to Browse tab
            if (getActivity() != null) {
                // Set the bottom navigation to Browse tab (index 0)
                ((com.example.educatro.MainActivity) getActivity()).navigateToTab(0);
            }
        });

        // Load downloaded courses
        loadDownloadedCourses();
    }

    private void setupRecyclerView() {
        downloadsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        courseAdapter = new CourseAdapter(new ArrayList<>(), new CourseAdapter.OnCourseClickListener() {
            @Override
            public void onCourseClick(Course course) {
                navigateToCourseDetail(course);
            }
        });
        downloadsRecyclerView.setAdapter(courseAdapter);
    }

    private void loadDownloadedCourses() {
        showLoading();

        usersRef.child(userId).child("downloadedCourses").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                downloadedCourseIds.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String courseId = snapshot.getValue(String.class);
                    if (courseId != null) {
                        downloadedCourseIds.add(courseId);
                    }
                }

                if (downloadedCourseIds.isEmpty()) {
                    showEmptyWithDummyCourses();
                } else {
                    loadCourseDetails();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                showError("Error loading downloaded courses: " + databaseError.getMessage());
            }
        });
    }

    private void loadCourseDetails() {
        final List<Course> downloadedCourses = new ArrayList<>();

        for (String courseId : downloadedCourseIds) {
            coursesRef.child(courseId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Course course = dataSnapshot.getValue(Course.class);
                    if (course != null) {
                        course.setDownloaded(true);
                        downloadedCourses.add(course);

                        // Update adapter when all courses are loaded
                        if (downloadedCourses.size() == downloadedCourseIds.size()) {
                            updateAdapter(downloadedCourses);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    showError("Error loading course details: " + databaseError.getMessage());
                }
            });
        }
    }

    private void showEmptyWithDummyCourses() {
        // Create dummy courses for display
        List<Course> dummyCourses = createDummyCourses();
        
        // Show empty state message and explore button
        loadingProgressBar.setVisibility(View.GONE);
        errorTextView.setVisibility(View.GONE);
        emptyTextView.setVisibility(View.VISIBLE);
        exploreBrowseButton.setVisibility(View.VISIBLE);
        downloadsRecyclerView.setVisibility(View.VISIBLE);
        
        // Update adapter with dummy courses
        updateAdapter(dummyCourses);
    }
    
    private List<Course> createDummyCourses() {
        List<Course> dummyCourses = new ArrayList<>();
        
        // Dummy Course 1 (Downloaded)
        Course course1 = new Course();
        course1.setCourseId("download_dummy1");
        course1.setTitle("Mobile App Development with Flutter");
        course1.setCategory("Development");
        course1.setDescription("Build beautiful cross-platform mobile applications with Flutter.");
        course1.setImageUrl("https://img-c.udemycdn.com/course/750x422/2381802_d90c_5.jpg");
        course1.setAuthorName("Alex Rodriguez");
        course1.setDuration(240); // 4 hours
        course1.setLessonsCount(15);
        course1.setRating(4.9f);
        course1.setRatingsCount(320);
        course1.setPrice(79.99);
        course1.setDownloaded(true); // Mark as downloaded
        
        // Dummy Course 2 (Downloaded)
        Course course2 = new Course();
        course2.setCourseId("download_dummy2");
        course2.setTitle("Machine Learning Fundamentals");
        course2.setCategory("Data Science");
        course2.setDescription("Learn the basics of machine learning with practical examples.");
        course2.setImageUrl("https://img-c.udemycdn.com/course/750x422/950390_270f_3.jpg");
        course2.setAuthorName("Jennifer Kim");
        course2.setDuration(300); // 5 hours
        course2.setLessonsCount(18);
        course2.setRating(4.7f);
        course2.setRatingsCount(270);
        course2.setPrice(89.99);
        course2.setDownloaded(true); // Mark as downloaded
        
        // Add dummy courses to the list
        dummyCourses.add(course1);
        dummyCourses.add(course2);
        
        return dummyCourses;
    }

    private void updateAdapter(List<Course> courses) {
        courseAdapter.setCourses(courses);
        hideLoading();
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
        emptyTextView.setVisibility(View.GONE);
        exploreBrowseButton.setVisibility(View.GONE);
        downloadsRecyclerView.setVisibility(View.GONE);
    }

    private void hideLoading() {
        loadingProgressBar.setVisibility(View.GONE);
        downloadsRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showError(String errorMessage) {
        loadingProgressBar.setVisibility(View.GONE);
        errorTextView.setVisibility(View.VISIBLE);
        errorTextView.setText(errorMessage);
        emptyTextView.setVisibility(View.GONE);
        exploreBrowseButton.setVisibility(View.GONE);
        downloadsRecyclerView.setVisibility(View.GONE);
    }
} 