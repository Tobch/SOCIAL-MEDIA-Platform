package com.example.demo8;

public class Comment
{
    private static int nextCommentId = 1; // Static variable to track the next comment ID
    private int id; // Comment ID
    private String commenterUsername; // Username of the commenter
    private String commentContent; // Content of the comment



    // Constructor to initialize comment properties
    public Comment(String commenterUsername, String commentContent) {
        this.id = nextCommentId++; // Assign comment ID and increment the counter
        this.commenterUsername = commenterUsername;
        this.commentContent = commentContent;
    }

    // Getter method for commenterUsername
    public String getCommenterUsername() {
        return commenterUsername;
    }

    // Getter method for commentContent
    public String getCommentContent() {
        return commentContent;
    }

    // Override toString method to display comment information
    @Override
    public String toString() {
        return "Comment ID: " + id + ", Commenter: " + commenterUsername + ", Content: " + commentContent;
    }




}