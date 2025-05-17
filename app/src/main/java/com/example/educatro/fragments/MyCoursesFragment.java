package com.example.educatro.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class MyCoursesFragment extends Fragment {

    private RecyclerView inProgressRecyclerView;
    private RecyclerView completedRecyclerView;
    private ProgressBar loadingProgressBar;
    private TextView errorTextView;
    private TextView emptyTextView;

    private CourseAdapter inProgressAdapter;
    private CourseAdapter completedAdapter;

    private FirebaseDatabase database;
    private DatabaseReference usersRef;
    private DatabaseReference coursesRef;

    private String userId;
    private List<String> enrolledCourseIds = new ArrayList<>();

    public MyCoursesFragment() {
        // Required empty public constructor
    }

    public static MyCoursesFragment newInstance(String userId) {
        MyCoursesFragment fragment = new MyCoursesFragment();
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
        return inflater.inflate(R.layout.fragment_my_courses, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        inProgressRecyclerView = view.findViewById(R.id.inProgressRecyclerView);
        completedRecyclerView = view.findViewById(R.id.completedRecyclerView);
        loadingProgressBar = view.findViewById(R.id.loadingProgressBar);
        errorTextView = view.findViewById(R.id.errorTextView);
        emptyTextView = view.findViewById(R.id.emptyTextView);

        // Set up RecyclerViews
        setupInProgressRecyclerView();
        setupCompletedRecyclerView();

        // Load enrolled courses
        loadEnrolledCourses();
    }

    private void setupInProgressRecyclerView() {
        inProgressRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        inProgressAdapter = new CourseAdapter(new ArrayList<>(), new CourseAdapter.OnCourseClickListener() {
            @Override
            public void onCourseClick(Course course) {
                navigateToCourseDetail(course);
            }
        });
        inProgressRecyclerView.setAdapter(inProgressAdapter);
    }

    private void setupCompletedRecyclerView() {
        completedRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        completedAdapter = new CourseAdapter(new ArrayList<>(), new CourseAdapter.OnCourseClickListener() {
            @Override
            public void onCourseClick(Course course) {
                navigateToCourseDetail(course);
            }
        });
        completedRecyclerView.setAdapter(completedAdapter);
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
                    showEmpty();
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
        final List<Course> inProgressCourses = new ArrayList<>();
        final List<Course> completedCourses = new ArrayList<>();

        for (String courseId : enrolledCourseIds) {
            coursesRef.child(courseId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Course course = dataSnapshot.getValue(Course.class);
                    if (course != null) {
                        // In a real app, you would check the progress
                        // For demo, we'll consider courses with progress > 0 and < 100 as in progress
                        // and courses with progress = 100 as completed
                        if (course.getProgressPercentage() >= 100) {
                            completedCourses.add(course);
                        } else {
                            inProgressCourses.add(course);
                        }

                        // Update adapters when all courses are loaded
                        if (inProgressCourses.size() + completedCourses.size() == enrolledCourseIds.size()) {
                            updateAdapters(inProgressCourses, completedCourses);
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

    private void updateAdapters(List<Course> inProgressCourses, List<Course> completedCourses) {
        inProgressAdapter.setCourses(inProgressCourses);
        completedAdapter.setCourses(completedCourses);
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
    }

    private void hideLoading() {
        loadingProgressBar.setVisibility(View.GONE);
    }

    private void showError(String errorMessage) {
        loadingProgressBar.setVisibility(View.GONE);
        errorTextView.setVisibility(View.VISIBLE);
        errorTextView.setText(errorMessage);
        emptyTextView.setVisibility(View.GONE);
    }

    private void showEmpty() {
        loadingProgressBar.setVisibility(View.GONE);
        errorTextView.setVisibility(View.GONE);
        emptyTextView.setVisibility(View.VISIBLE);
    }
} 