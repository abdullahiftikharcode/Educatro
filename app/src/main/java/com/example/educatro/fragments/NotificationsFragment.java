package com.example.educatro.fragments;

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

import com.example.educatro.R;

public class NotificationsFragment extends Fragment {

    private RecyclerView notificationsRecyclerView;
    private ProgressBar loadingProgressBar;
    private TextView errorTextView;
    private TextView emptyTextView;
    private String userId;

    public NotificationsFragment() {
        // Required empty public constructor
    }

    public static NotificationsFragment newInstance(String userId) {
        NotificationsFragment fragment = new NotificationsFragment();
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
        return inflater.inflate(R.layout.fragment_notifications, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        notificationsRecyclerView = view.findViewById(R.id.notificationsRecyclerView);
        loadingProgressBar = view.findViewById(R.id.loadingProgressBar);
        errorTextView = view.findViewById(R.id.errorTextView);
        emptyTextView = view.findViewById(R.id.emptyTextView);

        // Setup RecyclerView
        setupRecyclerView();

        // Load notifications - for now show empty state
        showEmpty();
    }

    private void setupRecyclerView() {
        notificationsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // In a real app, you would create a NotificationsAdapter and set it here
    }

    private void showLoading() {
        loadingProgressBar.setVisibility(View.VISIBLE);
        errorTextView.setVisibility(View.GONE);
        emptyTextView.setVisibility(View.GONE);
        notificationsRecyclerView.setVisibility(View.GONE);
    }

    private void hideLoading() {
        loadingProgressBar.setVisibility(View.GONE);
        notificationsRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showError(String errorMessage) {
        loadingProgressBar.setVisibility(View.GONE);
        errorTextView.setVisibility(View.VISIBLE);
        errorTextView.setText(errorMessage);
        emptyTextView.setVisibility(View.GONE);
        notificationsRecyclerView.setVisibility(View.GONE);
    }

    private void showEmpty() {
        loadingProgressBar.setVisibility(View.GONE);
        errorTextView.setVisibility(View.GONE);
        emptyTextView.setVisibility(View.VISIBLE);
        notificationsRecyclerView.setVisibility(View.GONE);
    }
} 