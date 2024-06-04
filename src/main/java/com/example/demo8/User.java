package com.example.demo8;

import java.util.ArrayList;
import java.util.List;

public  class User {
    private String username;
    private String password;
    private String bio;
    String Email;
    private String profilePicture;
    private String publicInfo;
    private List<String> friends;
    private List<String> following;
    private List<String> posts;

    // Constructor to initialize user properties
    public User(String username, String password, String bio,String Email, String profilePicture,String publicInfo)
    {
        this.username = username;
        this.Email = Email;
        this.password = password;
        this.bio = bio;
        this.publicInfo = publicInfo;
        this.profilePicture = profilePicture;
        this.friends = new ArrayList<>();
        this.following = new ArrayList<>();
        this.posts = new ArrayList<>();
    }

    // Getter for password (not used in the project)
    public String getPassword() {
        return password;
    }

    // Method to set public information for the user
    public void setPublicInfo(String publicInfo)
    {
        this.publicInfo = publicInfo;
    }

    // Method to add a friend for the user
    public void addFriend(String friendUsername)
    {
        friends.add(friendUsername);
    }
    public void removeFriend(String friendUsername)
    {
        friends.remove(friendUsername);
    }

    public void addPost(String friendUsername)
    {
        posts.add(friendUsername);
    }

    // Method to get the list of friends for the user
    public List<String> getFriends()
    {
        return friends;
    }

    // Method to follow another user
    public void followUser(String usernameToFollow) {
        following.add(usernameToFollow);
    }

    public void unfollowUser(String usernameToFollow) {
        following.remove(usernameToFollow);
    }

    // Method to get the list of users followed by the user
    public List<String> getFollowing()
    {
        return following;
    }

    public String getBio() {
        return bio;
    }

    public String getUsername() {
        return username;
    }

    public String getPublicInfo() {
        return publicInfo;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getEmail() {
        return Email;
    }

    public String getProfilePicture() {
        return profilePicture;
    }
    public String firends() {
        StringBuilder sb = new StringBuilder();
       // sb.append("Post ID: ").append(id).append("\n");
        //sb.append("Author: ").append(author).append("\n");
        //sb.append("Content: ").append(content).append("\n");
        sb.append("friends : ").append(friends).append("\n");
        return sb.toString();
    }
    public String follwing() {
        StringBuilder sb = new StringBuilder();
        // sb.append("Post ID: ").append(id).append("\n");
        //sb.append("Author: ").append(author).append("\n");
        //sb.append("Content: ").append(content).append("\n");
        sb.append("following : ").append(following).append("\n");
        return sb.toString();
    }
    public String posts() {
        StringBuilder sb = new StringBuilder();
        // sb.append("Post ID: ").append(id).append("\n");
        //sb.append("Author: ").append(author).append("\n");
        //sb.append("Content: ").append(content).append("\n");
        sb.append("following : ").append(posts).append("\n");
        return sb.toString();
    }

}
