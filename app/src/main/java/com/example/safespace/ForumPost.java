package com.example.safespace;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ForumPost implements Serializable {
    private String id;
    private String subject;  // From old code
    private String title;    // From new code, optional alias for subject
    private String content;
    private String username;
    private String userProfileImageUrl;  // From old code (URL)
    private String tag;                  // Single tag from old code
    private List<String> categories;     // From new code (List of tags)
    private long timestamp;             // From old code
    private String timePosted;          // Optional string-based time for display
    private Integer imageResource;      // From new code (local resource ID)
    private int likeCount;
    private int commentCount;
    private int shareCount;

    // Required empty constructor for Firebase
    public ForumPost() {}

    // Constructor for basic creation (old style)
    public ForumPost(String subject, String content, String tag, String username) {
        this.subject = subject;
        this.title = subject;  // To keep both subject and title in sync
        this.content = content;
        this.tag = tag;
        this.username = username;
        this.timestamp = System.currentTimeMillis();
        this.timePosted = getFormattedTimestamp();
    }

    // Full constructor (with all new fields)
    public ForumPost(String username, String timePosted, String title, String content,
                     Integer imageResource, List<String> categories,
                     int likeCount, int commentCount, int shareCount) {
        this.username = username;
        this.timePosted = timePosted;
        this.title = title;
        this.subject = title; // Keep subject and title consistent
        this.content = content;
        this.imageResource = imageResource;
        this.categories = categories;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.shareCount = shareCount;
        this.timestamp = System.currentTimeMillis();  // Optional, can be overridden
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) {
        this.subject = subject;
        this.title = subject;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) {
        this.title = title;
        this.subject = title;
    }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getUserProfileImageUrl() { return userProfileImageUrl; }
    public void setUserProfileImageUrl(String userProfileImageUrl) {
        this.userProfileImageUrl = userProfileImageUrl;
    }

    public String getTag() { return tag; }
    public void setTag(String tag) { this.tag = tag; }

    public List<String> getCategories() { return categories; }
    public void setCategories(List<String> categories) { this.categories = categories; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public String getTimePosted() { return timePosted; }
    public void setTimePosted(String timePosted) { this.timePosted = timePosted; }

    public Integer getImageResource() { return imageResource; }
    public void setImageResource(Integer imageResource) { this.imageResource = imageResource; }

    public int getLikes() { return likeCount; }
    public void setLikes(int likeCount) { this.likeCount = likeCount; }

    public int getComments() { return commentCount; }
    public void setComments(int commentCount) { this.commentCount = commentCount; }

    public int getShares() { return shareCount; }
    public void setShares(int shareCount) { this.shareCount = shareCount; }

    // Formatted time string (from timestamp)
    public String getFormattedTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }

    // "Time ago" helper
    public String getTimeAgo() {
        long currentTime = System.currentTimeMillis();
        long diffTime = currentTime - timestamp;

        long seconds = diffTime / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        if (days > 0) {
            return days + " day" + (days > 1 ? "s" : "") + " ago";
        } else if (hours > 0) {
            return hours + " hour" + (hours > 1 ? "s" : "") + " ago";
        } else if (minutes > 0) {
            return minutes + " min" + (minutes > 1 ? "s" : "") + " ago";
        } else {
            return "just now";
        }
    }

    // Safe fallback ID method if needed
    public String getGeneratedId() {
        return (username != null ? username : "user") + "_" + timestamp;
    }
}
