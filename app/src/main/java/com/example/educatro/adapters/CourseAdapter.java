package com.example.educatro.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.educatro.R;
import com.example.educatro.models.Course;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

    private List<Course> courses;
    private OnCourseClickListener listener;

    public interface OnCourseClickListener {
        void onCourseClick(Course course);
    }

    public CourseAdapter(List<Course> courses, OnCourseClickListener listener) {
        this.courses = courses;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Course course = courses.get(position);
        
        holder.titleTextView.setText(course.getTitle());
        holder.authorTextView.setText(course.getAuthorName());
        holder.ratingBar.setRating(course.getRating());
        holder.ratingCountTextView.setText(String.format("(%d)", course.getRatingsCount()));
        holder.lessonsCountTextView.setText(String.format("%d %s", course.getLessonsCount(), holder.itemView.getContext().getString(R.string.lessons)));
        
        // Load image with Glide
        if (course.getImageUrl() != null && !course.getImageUrl().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(course.getImageUrl())
                    .placeholder(R.drawable.placeholder_course)
                    .error(R.drawable.placeholder_course)
                    .centerCrop()
                    .into(holder.courseImageView);
        } else {
            holder.courseImageView.setImageResource(R.drawable.placeholder_course);
        }
        
        // Set bookmark icon based on bookmark status
        holder.bookmarkIcon.setImageResource(course.isBookmarked() ? 
                R.drawable.ic_bookmark_filled : R.drawable.ic_bookmark);
        
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onCourseClick(course);
                }
            }
        });
        
        holder.bookmarkIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle bookmark status
                course.setBookmarked(!course.isBookmarked());
                notifyItemChanged(holder.getAdapterPosition());
                
                // In a real app, you would update the database here
            }
        });
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
        notifyDataSetChanged();
    }

    static class CourseViewHolder extends RecyclerView.ViewHolder {
        ImageView courseImageView;
        TextView titleTextView;
        TextView authorTextView;
        RatingBar ratingBar;
        TextView ratingCountTextView;
        TextView lessonsCountTextView;
        ImageView bookmarkIcon;

        CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            courseImageView = itemView.findViewById(R.id.courseImageView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            authorTextView = itemView.findViewById(R.id.authorTextView);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            ratingCountTextView = itemView.findViewById(R.id.ratingCountTextView);
            lessonsCountTextView = itemView.findViewById(R.id.lessonsCountTextView);
            bookmarkIcon = itemView.findViewById(R.id.bookmarkIcon);
        }
    }
} 