package com.example.educatro.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.educatro.R;
import com.example.educatro.models.Course;

import java.util.List;

public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.LessonViewHolder> {

    private List<Course.Lesson> lessons;
    private OnLessonClickListener listener;

    public interface OnLessonClickListener {
        void onLessonClick(Course.Lesson lesson);
    }

    public LessonAdapter(List<Course.Lesson> lessons) {
        this.lessons = lessons;
    }

    public LessonAdapter(List<Course.Lesson> lessons, OnLessonClickListener listener) {
        this.lessons = lessons;
        this.listener = listener;
    }

    @NonNull
    @Override
    public LessonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lesson, parent, false);
        return new LessonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LessonViewHolder holder, int position) {
        Course.Lesson lesson = lessons.get(position);
        
        holder.titleTextView.setText(lesson.getTitle());
        holder.durationTextView.setText(formatDuration(lesson.getDuration()));
        
        // Set completion status
        holder.statusIcon.setImageResource(lesson.isCompleted() ? 
                R.drawable.ic_lesson_completed : R.drawable.ic_lesson_not_completed);
        
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onLessonClick(lesson);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return lessons.size();
    }

    public void setLessons(List<Course.Lesson> lessons) {
        this.lessons = lessons;
        notifyDataSetChanged();
    }

    private String formatDuration(int minutes) {
        int hours = minutes / 60;
        int mins = minutes % 60;
        if (hours > 0) {
            return String.format("%dh %dm", hours, mins);
        } else {
            return String.format("%dm", mins);
        }
    }

    static class LessonViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView durationTextView;
        ImageView statusIcon;

        LessonViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            durationTextView = itemView.findViewById(R.id.durationTextView);
            statusIcon = itemView.findViewById(R.id.statusIcon);
        }
    }
} 