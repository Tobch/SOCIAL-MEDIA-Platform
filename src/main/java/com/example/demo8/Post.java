package com.example.demo8;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Post implements Serializable {
    private static int nextPostId = 1;
    private int id;
    private String content;
    private String author;
    private List<Comment> comments;
    private  Set<String> likes;

    public Post(String author, String content) {
        this.id = nextPostId++;
        this.author = author;
        this.content = content;
        this.comments = new ArrayList<>();
        this.likes = new HashSet<>();
    }

    // Getter for post ID
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getAuthor() {
        return author;
    }
    public String getContent() {
        return content;
    }
    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public void setLikes(Set<String> likes) {
        this.likes = likes;
    }

    // Method to add a comment to the post
    public void addComment(String commenterUsername, String commentContent) {
        Comment comment = new Comment(commenterUsername, commentContent);
        comments.add(comment);
    }

    // Method to add a like to the post
    public void addLike(String username) {
        likes.add(username);
    }

    // Method to get the list of comments on the post
    public List<Comment> getComments() {
        return comments;
    }

    // Method to get the set of users who liked the post
    public  Set<String> getLikes() {
        return likes;
    }
    public int getLikeCount()
    {
        return likes.size();
    }

    // Override toString method to display post information
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Post ID: ").append(id).append("\n");
        sb.append("Author: ").append(author).append("\n");
        sb.append("Content: ").append(content).append("\n");
        sb.append("Comments: ").append(comments).append("\n");
        sb.append("Likes: ").append(likes.size()).append("\n");
        return sb.toString();
    }

    public static int getNextPostId() {
        return nextPostId;
    }

}