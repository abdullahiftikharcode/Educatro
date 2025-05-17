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

public class BookmarksFragment extends Fragment {

    private RecyclerView bookmarksRecyclerView;
    private ProgressBar loadingProgressBar;
    private TextView errorTextView;
    private TextView emptyTextView;

    private CourseAdapter courseAdapter;

    private FirebaseDatabase database;
    private DatabaseReference usersRef;
    private DatabaseReference coursesRef;

    private String userId;
    private List<String> bookmarkedCourseIds = new ArrayList<>();

    public BookmarksFragment() {
        // Required empty public constructor
    }

    public static BookmarksFragment newInstance(String userId) {
        BookmarksFragment fragment = new BookmarksFragment();
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
        return inflater.inflate(R.layout.fragment_bookmarks, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        bookmarksRecyclerView = view.findViewById(R.id.bookmarksRecyclerView);
        loadingProgressBar = view.findViewById(R.id.loadingProgressBar);
        errorTextView = view.findViewById(R.id.errorTextView);
        emptyTextView = view.findViewById(R.id.emptyTextView);

        // Set up RecyclerView
        setupRecyclerView();

        // Load bookmarked courses
        loadBookmarkedCourses();
    }

    private void setupRecyclerView() {
        bookmarksRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        courseAdapter = new CourseAdapter(new ArrayList<>(), new CourseAdapter.OnCourseClickListener() {
            @Override
            public void onCourseClick(Course course) {
                navigateToCourseDetail(course);
            }
        });
        bookmarksRecyclerView.setAdapter(courseAdapter);
    }

    private void loadBookmarkedCourses() {
        showLoading();

        usersRef.child(userId).child("bookmarkedCourses").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bookmarkedCourseIds.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String courseId = snapshot.getValue(String.class);
                    if (courseId != null) {
                        bookmarkedCourseIds.add(courseId);
                    }
                }

                if (bookmarkedCourseIds.isEmpty()) {
                    showEmpty();
                } else {
                    loadCourseDetails();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                showError("Error loading bookmarked courses: " + databaseError.getMessage());
            }
        });
    }

    private void loadCourseDetails() {
        final List<Course> bookmarkedCourses = new ArrayList<>();

        for (String courseId : bookmarkedCourseIds) {
            coursesRef.child(courseId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Course course = dataSnapshot.getValue(Course.class);
                    if (course != null) {
                        course.setBookmarked(true); // Mark as bookmarked
                        bookmarkedCourses.add(course);

                        // Update adapter when all courses are loaded
                        if (bookmarkedCourses.size() == bookmarkedCourseIds.size()) {
                            updateAdapter(bookmarkedCourses);
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

    private void updateAdapter(List<Course> bookmarkedCourses) {
        courseAdapter.setCourses(bookmarkedCourses);
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