package com.example.educatro.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Course {
    private String courseId;
    private String title;
    private String category;
    private String description;
    private String imageUrl;
    private String authorId;
    private String authorName;
    private int duration; // in minutes
    private int lessonsCount;
    private float rating;
    private int ratingsCount;
    private double price;
    private List<String> skills;
    private List<Lesson> lessons;
    private boolean isBookmarked;
    private boolean isDownloaded;
    private int progressPercentage;

    // Empty constructor needed for Firebase
    public Course() {
        skills = new ArrayList<>();
        lessons = new ArrayList<>();
    }

    public Course(String courseId, String title, String category, String description, 
                 String imageUrl, String authorId, String authorName, int duration, 
                 int lessonsCount, float rating, int ratingsCount, double price) {
        this.courseId = courseId;
        this.title = title;
        this.category = category;
        this.description = description;
        this.imageUrl = imageUrl;
        this.authorId = authorId;
        this.authorName = authorName;
        this.duration = duration;
        this.lessonsCount = lessonsCount;
        this.rating = rating;
        this.ratingsCount = ratingsCount;
        this.price = price;
        this.skills = new ArrayList<>();
        this.lessons = new ArrayList<>();
        this.isBookmarked = false;
        this.isDownloaded = false;
        this.progressPercentage = 0;
    }

    // Convert Course object to a Map for Firebase
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("courseId", courseId);
        result.put("title", title);
        result.put("category", category);
        result.put("description", description);
        result.put("imageUrl", imageUrl);
        result.put("authorId", authorId);
        result.put("authorName", authorName);
        result.put("duration", duration);
        result.put("lessonsCount", lessonsCount);
        result.put("rating", rating);
        result.put("ratingsCount", ratingsCount);
        result.put("price", price);
        result.put("skills", skills);
        // We don't store lessons in the course document directly
        // They will be in a subcollection
        return result;
    }

    // Getters and setters
    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getLessonsCount() {
        return lessonsCount;
    }

    public void setLessonsCount(int lessonsCount) {
        this.lessonsCount = lessonsCount;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getRatingsCount() {
        return ratingsCount;
    }

    public void setRatingsCount(int ratingsCount) {
        this.ratingsCount = ratingsCount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }

    public boolean isBookmarked() {
        return isBookmarked;
    }

    public void setBookmarked(boolean bookmarked) {
        isBookmarked = bookmarked;
    }

    public boolean isDownloaded() {
        return isDownloaded;
    }

    public void setDownloaded(boolean downloaded) {
        isDownloaded = downloaded;
    }

    public int getProgressPercentage() {
        return progressPercentage;
    }

    public void setProgressPercentage(int progressPercentage) {
        this.progressPercentage = progressPercentage;
    }

    // Inner class for lessons
    public static class Lesson {
        private String lessonId;
        private String title;
        private int duration; // in minutes
        private boolean isCompleted;

        public Lesson() {
        }

        public Lesson(String lessonId, String title, int duration) {
            this.lessonId = lessonId;
            this.title = title;
            this.duration = duration;
            this.isCompleted = false;
        }

        public String getLessonId() {
            return lessonId;
        }

        public void setLessonId(String lessonId) {
            this.lessonId = lessonId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public boolean isCompleted() {
            return isCompleted;
        }

        public void setCompleted(boolean completed) {
            isCompleted = completed;
        }
    }
} 