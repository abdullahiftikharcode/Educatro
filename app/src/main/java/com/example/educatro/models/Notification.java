package com.example.educatro.models;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Notification {
    private String notificationId;
    private String userId;
    private String title;
    private String message;
    private String imageUrl;
    private String type; // course, author, system, etc.
    private String relatedId; // courseId, authorId, etc.
    private Date timestamp;
    private boolean isRead;

    // Empty constructor needed for Firebase
    public Notification() {
    }

    public Notification(String notificationId, String userId, String title, String message, 
                       String imageUrl, String type, String relatedId) {
        this.notificationId = notificationId;
        this.userId = userId;
        this.title = title;
        this.message = message;
        this.imageUrl = imageUrl;
        this.type = type;
        this.relatedId = relatedId;
        this.timestamp = new Date();
        this.isRead = false;
    }

    // Convert Notification object to a Map for Firebase
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("notificationId", notificationId);
        result.put("userId", userId);
        result.put("title", title);
        result.put("message", message);
        result.put("imageUrl", imageUrl);
        result.put("type", type);
        result.put("relatedId", relatedId);
        result.put("timestamp", timestamp);
        result.put("isRead", isRead);
        return result;
    }

    // Getters and setters
    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRelatedId() {
        return relatedId;
    }

    public void setRelatedId(String relatedId) {
        this.relatedId = relatedId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
}