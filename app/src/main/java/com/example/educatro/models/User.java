package com.example.educatro.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {
    private String userId;
    private String name;
    private String email;
    private String password; // We'll store the password in the database for this implementation
    private List<String> enrolledCourses;
    private List<String> bookmarkedCourses;
    private List<String> downloadedCourses;
    private List<String> favoriteCategories;
    private int followersCount;
    private int followingCount;
    private int certificatesCount;
    private int finishedCoursesCount;

    // Empty constructor needed for Firebase
    public User() {
        // Initialize lists to avoid null pointer exceptions
        enrolledCourses = new ArrayList<>();
        bookmarkedCourses = new ArrayList<>();
        downloadedCourses = new ArrayList<>();
        favoriteCategories = new ArrayList<>();
    }

    public User(String userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.enrolledCourses = new ArrayList<>();
        this.bookmarkedCourses = new ArrayList<>();
        this.downloadedCourses = new ArrayList<>();
        this.favoriteCategories = new ArrayList<>();
        this.followersCount = 0;
        this.followingCount = 0;
        this.certificatesCount = 0;
        this.finishedCoursesCount = 0;
    }

    // Constructor with password for authentication purposes
    public User(String userId, String name, String email, String password) {
        this(userId, name, email);
        this.password = password;
    }

    // Convert User object to a Map for Firebase
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("userId", userId);
        result.put("name", name);
        result.put("email", email);
        result.put("password", password); // Include password in the database
        result.put("enrolledCourses", enrolledCourses);
        result.put("bookmarkedCourses", bookmarkedCourses);
        result.put("downloadedCourses", downloadedCourses);
        result.put("favoriteCategories", favoriteCategories);
        result.put("followersCount", followersCount);
        result.put("followingCount", followingCount);
        result.put("certificatesCount", certificatesCount);
        result.put("finishedCoursesCount", finishedCoursesCount);
        return result;
    }

    // Getters and setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getEnrolledCourses() {
        return enrolledCourses;
    }

    public void setEnrolledCourses(List<String> enrolledCourses) {
        this.enrolledCourses = enrolledCourses;
    }

    public List<String> getBookmarkedCourses() {
        return bookmarkedCourses;
    }

    public void setBookmarkedCourses(List<String> bookmarkedCourses) {
        this.bookmarkedCourses = bookmarkedCourses;
    }

    public List<String> getDownloadedCourses() {
        return downloadedCourses;
    }

    public void setDownloadedCourses(List<String> downloadedCourses) {
        this.downloadedCourses = downloadedCourses;
    }

    public List<String> getFavoriteCategories() {
        return favoriteCategories;
    }

    public void setFavoriteCategories(List<String> favoriteCategories) {
        this.favoriteCategories = favoriteCategories;
    }
    
    // Custom setter to handle Firebase HashMap to List conversion
    public void setFavoriteCategories(Map<String, Object> categoriesMap) {
        if (categoriesMap == null) {
            this.favoriteCategories = new ArrayList<>();
            return;
        }
        
        List<String> categories = new ArrayList<>();
        for (Object value : categoriesMap.values()) {
            if (value instanceof String) {
                categories.add((String) value);
            }
        }
        this.favoriteCategories = categories;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(int followingCount) {
        this.followingCount = followingCount;
    }

    public int getCertificatesCount() {
        return certificatesCount;
    }

    public void setCertificatesCount(int certificatesCount) {
        this.certificatesCount = certificatesCount;
    }

    public int getFinishedCoursesCount() {
        return finishedCoursesCount;
    }

    public void setFinishedCoursesCount(int finishedCoursesCount) {
        this.finishedCoursesCount = finishedCoursesCount;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
} 