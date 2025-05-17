package com.example.educatro.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Author {
    private String authorId;
    private String name;
    private String profileImageUrl;
    private String category;
    private String bio;
    private float rating;
    private int ratingsCount;
    private int followersCount;
    private int followingCount;
    private boolean isFollowing;
    private List<String> socialLinks;
    private List<String> courses;

    // Empty constructor needed for Firebase
    public Author() {
        socialLinks = new ArrayList<>();
        courses = new ArrayList<>();
    }

    public Author(String authorId, String name, String profileImageUrl, String category, String bio) {
        this.authorId = authorId;
        this.name = name;
        this.profileImageUrl = profileImageUrl;
        this.category = category;
        this.bio = bio;
        this.rating = 0;
        this.ratingsCount = 0;
        this.followersCount = 0;
        this.followingCount = 0;
        this.isFollowing = false;
        this.socialLinks = new ArrayList<>();
        this.courses = new ArrayList<>();
    }

    // Convert Author object to a Map for Firebase
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("authorId", authorId);
        result.put("name", name);
        result.put("profileImageUrl", profileImageUrl);
        result.put("category", category);
        result.put("bio", bio);
        result.put("rating", rating);
        result.put("ratingsCount", ratingsCount);
        result.put("followersCount", followersCount);
        result.put("followingCount", followingCount);
        result.put("socialLinks", socialLinks);
        result.put("courses", courses);
        return result;
    }

    // Getters and setters
    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
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

    public boolean isFollowing() {
        return isFollowing;
    }

    public void setFollowing(boolean following) {
        isFollowing = following;
    }

    public List<String> getSocialLinks() {
        return socialLinks;
    }

    public void setSocialLinks(List<String> socialLinks) {
        this.socialLinks = socialLinks;
    }

    public List<String> getCourses() {
        return courses;
    }

    public void setCourses(List<String> courses) {
        this.courses = courses;
    }
} 