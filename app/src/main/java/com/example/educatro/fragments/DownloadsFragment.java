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
import androidx.recyclerview.widget.GridLayoutManager;
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

public class DownloadsFragment extends Fragment {

    private RecyclerView downloadsRecyclerView;
    private ProgressBar loadingProgressBar;
    private TextView errorTextView;
    private TextView emptyTextView;

    private CourseAdapter courseAdapter;

    private FirebaseDatabase database;
    private DatabaseReference usersRef;
    private DatabaseReference coursesRef;

    private String userId;
    private List<String> downloadedCourseIds = new ArrayList<>();

    public DownloadsFragment() {
        // Required empty public constructor
    }

    public static DownloadsFragment newInstance(String userId) {
        DownloadsFragment fragment = new DownloadsFragment();
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
        return inflater.inflate(R.layout.fragment_downloads, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        downloadsRecyclerView = view.findViewById(R.id.downloadsRecyclerView);
        loadingProgressBar = view.findViewById(R.id.loadingProgressBar);
        errorTextView = view.findViewById(R.id.errorTextView);
        emptyTextView = view.findViewById(R.id.emptyTextView);

        // Set up RecyclerView
        setupRecyclerView();

        // Load downloaded courses
        loadDownloadedCourses();
    }

    private void setupRecyclerView() {
        downloadsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
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
                    showEmpty();
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
                        course.setDownloaded(true); // Mark as downloaded
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

    private void updateAdapter(List<Course> downloadedCourses) {
        courseAdapter.setCourses(downloadedCourses);
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