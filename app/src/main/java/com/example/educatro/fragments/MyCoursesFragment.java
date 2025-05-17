package com.example.educatro.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.educatro.CourseDetailActivity;
import com.example.educatro.R;
import com.example.educatro.adapters.CourseAdapter;
import com.example.educatro.adapters.MyCoursesTabAdapter;
import com.example.educatro.models.Course;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyCoursesFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private ImageButton filterButton;
    private ImageButton searchButton;
    private ProgressBar loadingProgressBar;
    private TextView errorTextView;
    private TextView emptyTextView;

    private MyCoursesTabAdapter tabAdapter;
    private String userId;

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
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);
        filterButton = view.findViewById(R.id.filterButton);
        searchButton = view.findViewById(R.id.searchButton);
        loadingProgressBar = view.findViewById(R.id.loadingProgressBar);
        errorTextView = view.findViewById(R.id.errorTextView);
        emptyTextView = view.findViewById(R.id.emptyTextView);

        // Setup filter and search button click listeners
        setupClickListeners();

        // Setup ViewPager and TabLayout
        setupTabLayout();
    }

    private void setupClickListeners() {
        filterButton.setOnClickListener(v -> {
            // Handle filter button click
        });

        searchButton.setOnClickListener(v -> {
            // Handle search button click
        });
    }

    private void setupTabLayout() {
        // Create tab adapter
        tabAdapter = new MyCoursesTabAdapter(this, userId);
        viewPager.setAdapter(tabAdapter);

        // Connect TabLayout with ViewPager2
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText(getString(R.string.courses));
                    break;
                case 1:
                    tab.setText(getString(R.string.downloads));
                    break;
            }
        }).attach();
    }
} 