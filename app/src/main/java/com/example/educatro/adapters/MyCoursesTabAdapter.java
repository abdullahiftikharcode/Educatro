package com.example.educatro.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.educatro.fragments.CoursesTabFragment;
import com.example.educatro.fragments.DownloadsTabFragment;
import com.example.educatro.fragments.MyCoursesFragment;

public class MyCoursesTabAdapter extends FragmentStateAdapter {

    private final String userId;

    public MyCoursesTabAdapter(@NonNull MyCoursesFragment fragment, String userId) {
        super(fragment);
        this.userId = userId;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return CoursesTabFragment.newInstance(userId);
            case 1:
                return DownloadsTabFragment.newInstance(userId);
            default:
                return CoursesTabFragment.newInstance(userId);
        }
    }

    @Override
    public int getItemCount() {
        return 2; // Two tabs: Courses and Downloads
    }
} 