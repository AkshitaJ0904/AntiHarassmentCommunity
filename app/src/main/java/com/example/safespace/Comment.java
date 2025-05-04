package com.example.safespace;

public class Comment {
    private String id;
    private String username;
    private String content;
    private String timestamp;
    private int likes;
    private int dislikes;
    private boolean hasReplies;

    public Comment(String id, String username, String content, String timestamp, int likes, int dislikes) {
        this.id = id;
        this.username = username;
        this.content = content;
        this.timestamp = timestamp;
        this.likes = likes;
        this.dislikes = dislikes;
        this.hasReplies = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    public boolean isHasReplies() {
        return hasReplies;
    }

    public void setHasReplies(boolean hasReplies) {
        this.hasReplies = hasReplies;
    }
}