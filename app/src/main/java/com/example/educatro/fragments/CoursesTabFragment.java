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

public class CoursesTabFragment extends Fragment {

    private RecyclerView coursesRecyclerView;
    private ProgressBar loadingProgressBar;
    private TextView errorTextView;
    private TextView emptyTextView;
    private Button exploreBrowseButton;

    private CourseAdapter courseAdapter;
    
    private FirebaseDatabase database;
    private DatabaseReference usersRef;
    private DatabaseReference coursesRef;
    
    private String userId;
    private List<String> enrolledCourseIds = new ArrayList<>();

    public CoursesTabFragment() {
        // Required empty public constructor
    }

    public static CoursesTabFragment newInstance(String userId) {
        CoursesTabFragment fragment = new CoursesTabFragment();
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
        return inflater.inflate(R.layout.fragment_courses_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        coursesRecyclerView = view.findViewById(R.id.coursesRecyclerView);
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

        // Load enrolled courses
        loadEnrolledCourses();
    }

    private void setupRecyclerView() {
        coursesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        courseAdapter = new CourseAdapter(new ArrayList<>(), new CourseAdapter.OnCourseClickListener() {
            @Override
            public void onCourseClick(Course course) {
                navigateToCourseDetail(course);
            }
        });
        coursesRecyclerView.setAdapter(courseAdapter);
    }

    private void loadEnrolledCourses() {
        showLoading();

        usersRef.child(userId).child("enrolledCourses").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                enrolledCourseIds.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String courseId = snapshot.getValue(String.class);
                    if (courseId != null) {
                        enrolledCourseIds.add(courseId);
                    }
                }

                if (enrolledCourseIds.isEmpty()) {
                    showEmptyWithDummyCourses();
                } else {
                    loadCourseDetails();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                showError("Error loading enrolled courses: " + databaseError.getMessage());
            }
        });
    }

    private void loadCourseDetails() {
        final List<Course> myCourses = new ArrayList<>();

        for (String courseId : enrolledCourseIds) {
            coursesRef.child(courseId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Course course = dataSnapshot.getValue(Course.class);
                    if (course != null) {
                        myCourses.add(course);

                        // Update adapter when all courses are loaded
                        if (myCourses.size() == enrolledCourseIds.size()) {
                            updateAdapter(myCourses);
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
        coursesRecyclerView.setVisibility(View.VISIBLE);
        
        // Update adapter with dummy courses
        updateAdapter(dummyCourses);
    }
    
    private List<Course> createDummyCourses() {
        List<Course> dummyCourses = new ArrayList<>();
        
        // Dummy Course 1
        Course course1 = new Course();
        course1.setCourseId("dummy1");
        course1.setTitle("UI/UX Design Fundamentals");
        course1.setCategory("Design");
        course1.setDescription("Learn the fundamentals of UI/UX design with this comprehensive course.");
        course1.setImageUrl("https://img-c.udemycdn.com/course/750x422/1565838_e54e_11.jpg");  // Use a placeholder URL
        course1.setAuthorName("Sarah Johnson");
        course1.setDuration(120); // 2 hours
        course1.setLessonsCount(8);
        course1.setRating(4.7f);
        course1.setRatingsCount(240);
        course1.setPrice(49.99);
        
        // Dummy Course 2
        Course course2 = new Course();
        course2.setCourseId("dummy2");
        course2.setTitle("Introduction to Web Development");
        course2.setCategory("Development");
        course2.setDescription("Get started with web development using HTML, CSS, and JavaScript.");
        course2.setImageUrl("https://img-c.udemycdn.com/course/750x422/792484_cc98_3.jpg"); // Use a placeholder URL
        course2.setAuthorName("Michael Brown");
        course2.setDuration(180); // 3 hours
        course2.setLessonsCount(12);
        course2.setRating(4.5f);
        course2.setRatingsCount(320);
        course2.setPrice(59.99);
        
        // Dummy Course 3
        Course course3 = new Course();
        course3.setCourseId("dummy3");
        course3.setTitle("Digital Marketing Essentials");
        course3.setCategory("Marketing");
        course3.setDescription("Master digital marketing strategies to grow your online presence.");
        course3.setImageUrl("https://img-c.udemycdn.com/course/750x422/903744_8eb2.jpg"); // Use a placeholder URL
        course3.setAuthorName("Emily Wilson");
        course3.setDuration(150); // 2.5 hours
        course3.setLessonsCount(10);
        course3.setRating(4.8f);
        course3.setRatingsCount(180);
        course3.setPrice(39.99);
        
        // Add dummy courses to the list
        dummyCourses.add(course1);
        dummyCourses.add(course2);
        dummyCourses.add(course3);
        
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
        coursesRecyclerView.setVisibility(View.GONE);
    }

    private void hideLoading() {
        loadingProgressBar.setVisibility(View.GONE);
        coursesRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showError(String errorMessage) {
        loadingProgressBar.setVisibility(View.GONE);
        errorTextView.setVisibility(View.VISIBLE);
        errorTextView.setText(errorMessage);
        emptyTextView.setVisibility(View.GONE);
        exploreBrowseButton.setVisibility(View.GONE);
        coursesRecyclerView.setVisibility(View.GONE);
    }
} 