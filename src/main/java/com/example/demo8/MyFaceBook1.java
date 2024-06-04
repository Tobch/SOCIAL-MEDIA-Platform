package com.example.demo8;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.FileReader;

// Main class fsor the Facebook project
public class MyFaceBook1
{
    private Scanner scanner; // Scanner for user input
    private Map<String, User> users; // Map to store users by username
    private Map<Integer, Post> posts; // Map to store posts by post ID
    private String loggedInUsername; // Username of the currently logged-in user


    // Constructor to initialize scanner and data structures
    public MyFaceBook1() {
        scanner = new Scanner(System.in);
        users = new HashMap<>(); // Initialize the map of users
        posts = new HashMap<>(); // Initialize the map of posts
        loadUserDataFromFile();
        loadPostsFromFile(); // Load posts from file
    }

    // Method to save login data to a file
    public void savelogindata(String username, String password) {
        try {
            FileWriter writer = new FileWriter("savelogindata.txt", true); // Append mode
            writer.write("Username: " + username + ", Password: " + password + "\n"); // Write username and password to the file
            writer.close();
            System.out.println("Username and password saved to file successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred while saving savelogindata to file.");
            e.printStackTrace();
        }
    }

    // Method to load user data from file
    private void loadUserDataFromFile() {
        try (Scanner fileScanner = new Scanner(new File("savelogindata.txt"))) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(", Password: ");
                if (parts.length == 2) {
                    String username = parts[0].replace("Username: ", "").trim();
                    String password = parts[1].trim();
                    User user = new User(username, password, "","", "","");
                    users.put(username, user); // Add user to the map of users
                } else {
                    System.out.println("Invalid format in savelogindata.txt: " + line);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("savelogindata file not found.");
            e.printStackTrace();
        }
    }

    private void loadPostsFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("posts.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Read data for each post
                int postId = Integer.parseInt(line.replace("Post ID: ", ""));
                String author = reader.readLine().replace("Author: ", "");
                String content = reader.readLine().replace("Content: ", "");

                // Read comments
                List<Comment> comments = new ArrayList<>();
                while ((line = reader.readLine()) != null && !line.isEmpty() && !line.equals("Likes:")) {
                    if (line.startsWith("\t- ")) {
                        String[] commentParts = line.split(": ", 2);
                        String commenterUsername = commentParts[0].replace("\t- ", "");
                        String commentContent = commentParts[1];
                        comments.add(new Comment(commenterUsername, commentContent));
                    }
                }

                // Read likes
                Set<String> likes = new HashSet<>();
                if (line != null && line.startsWith("Likes: ")) {
                    int likesCount = Integer.parseInt(line.replace("Likes: ", ""));
                    for (int i = 0; i < likesCount; i++) {
                        likes.add(reader.readLine());
                    }
                }

                // Create and add the post to the posts map
                Post post = new Post(author, content);
                post.setId(postId);
                post.setComments(comments);
                post.setLikes(likes);
                posts.put(postId, post);
            }
            System.out.println("Posts loaded from file successfully.");
        } catch (FileNotFoundException e) {
            System.out.println("posts.txt file not found.");
            // Log additional details or stack trace if needed
            e.printStackTrace();
            // Optionally, throw an exception here if you want to handle this as a critical error
        } catch (IOException e) {
            System.out.println("Error reading posts.txt file.");
            // Log additional details or stack trace if needed
            e.printStackTrace();
            // Optionally, throw an exception here if you want to handle this as a critical error
        }
    }

    private void savePostsToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("posts.txt"))) {
            for (Map.Entry<Integer, Post> entry : posts.entrySet()) {
                Post post = entry.getValue();
                writer.println("Post ID: " + post.getId());
                writer.println("Author: " + post.getAuthor());
                writer.println("Content: " + post.getContent());
                writer.println("Comments:");
                for (Comment comment : post.getComments()) {
                    writer.println("\t- " + comment.getCommenterUsername() + ": " + comment.getCommentContent());
                }
                writer.println("Likes: " + post.getLikes().size());
                writer.println(); // Add an empty line between posts
            }
            System.out.println("Posts saved to file successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred while saving posts to file.");
            e.printStackTrace();
        }
    }

    // Method to register a new user
    public void registerUser(String username,String password,String bio,String profilePicture) {
        System.out.print("Enter username: ");
        //String username = scanner.nextLine();
        System.out.print("Enter password: ");
        //String password = scanner.nextLine();
        System.out.print("Enter bio: ");
        //String bio = scanner.nextLine();
        System.out.print("Enter profile picture URL: ");
        //String profilePicture = scanner.nextLine();

        // Create a new user object
        User user = new User(username, password, bio,"ff", profilePicture,"");
        users.put(username, user); // Add user to the map of users
        savelogindata(username, password); // Save login data to file
        System.out.println("User registered successfully.");
    }

    // Method to log in a user
    public boolean login(String username,String password) {
        System.out.print("Enter username: ");
        //String username = scanner.nextLine();
        System.out.print("Enter password: ");
        //String password = scanner.nextLine();

        // Read usernames and passwords from file and compare with input
        try (Scanner fileScanner = new Scanner(new File("savelogindata.txt"))) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                // Split the line into username and password
                String[] parts = line.split(", Password: ");
                if (parts.length == 2) { // Ensure there are both username and password parts
                    String storedUsername = parts[0].replace("Username: ", "").trim();
                    String storedPassword = parts[1].trim();
                    // Check if entered username and password match the stored login data
                    if (username.equals(storedUsername) && password.equals(storedPassword)) {
                        loggedInUsername = username; // Set current user as logged in
                        return true;
                    }
                } else {
                    System.out.println("Invalid format in savelogindata.txt: " + line);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("savelogindata file not found.");
            e.printStackTrace();
        }
        // If no matching username and password found
        return false;
    }



    // Method to create a new post
    public void createPost() {
        if (loggedInUsername == null) {
            System.out.println("Please login first.");
            return;
        }
        // Prompt for user input
        System.out.print("Enter post content: ");
        String content = scanner.nextLine();

        // Create a new post object
        Post post = new Post(loggedInUsername, content);
        posts.put(post.getId(), post); // Add post to the map of posts
        System.out.println("Post created successfully.");
        savePostsToFile(); // Save posts to file after creating a new post
    }

    public void viewAllPostsWithLikesAndComments() {
        for (Post post : posts.values()) {
            System.out.println(post);
            System.out.println("Comments:");
            List<Comment> comments = post.getComments();
            if (comments.isEmpty()) {
                System.out.println("No comments yet.");
            } else {
                for (Comment comment : comments) {
                    System.out.println(comment);
                }
            }
            System.out.println("Likes: " + post.getLikes().size());
            System.out.println();
        }
    }

    // Method to add a comment to a post
    public void addComment(int postId) {
        Post post = posts.get(postId);
        if (post != null) {
            // Prompt for user input
            System.out.print("Enter your comment: ");
            String commentContent = scanner.nextLine();

            post.addComment(loggedInUsername, commentContent); // Add comment to the post
            System.out.println("Comment added successfully.");
            savePostsToFile(); // Save posts to file after adding a comment
        } else {
            System.out.println("Post with ID " + postId + " does not exist.");
        }
    }

    // Method to like a post
    public void likePost(int postId) {
        Post post = posts.get(postId);
        if (post != null) {
            post.addLike(loggedInUsername); // Add like to the post
            System.out.println("You liked the post.");
            savePostsToFile(); // Save posts to file after liking a post
        } else {
            System.out.println("Post with ID " + postId + " does not exist.");
        }
    }

    // Method to view posts and interact with them (comment/like)
    public void viewPosts() {
        for (Post post : posts.values()) {
            System.out.println(post); // Display the post
            System.out.println();

            // Check if user is logged in
            if (loggedInUsername != null) {
                // Prompt for user interaction
                System.out.print("Do you want to interact with this post? (comment/like/no): ");
                String interaction = scanner.nextLine();
                switch (interaction.toLowerCase()) {
                    case "comment":
                        addComment(post.getId());
                        break;
                    case "like":
                        likePost(post.getId());
                        break;
                    case "no":
                        break;
                    default:
                        System.out.println("Invalid option. No interaction performed.");
                }
            }
        }
    }

    // Method to enter public information about the user
    public void enterPublicInfo() {
        User user = users.get(loggedInUsername);
        if (user != null) {
            System.out.print("Enter public info about you: ");
            String publicInfo = scanner.nextLine();
            user.setPublicInfo(publicInfo); // Set public information for the user
            System.out.println("Public info updated successfully.");
        } else {
            System.out.println("User not found.");
        }
    }

    // Method to add a friend for the logged-in user
    public void addFriend() {
        if (loggedInUsername == null) {
            System.out.println("Please login first.");
            return;
        }
        System.out.print("Enter username of the friend to add: ");
        String friendUsername = scanner.nextLine();

        // Check if the friend username exists in the user data
        try (Scanner fileScanner = new Scanner(new File("savelogindata.txt"))) {
            boolean found = false;
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                // Split the line into username and password
                String[] parts = line.split(", Password: ");
                if (parts.length == 2) {
                    String storedUsername = parts[0].replace("Username: ", "").trim();
                    // Check if entered username matches the stored username
                    if (friendUsername.equals(storedUsername)) {
                        found = true;
                        break;
                    }
                } else {
                    System.out.println("Invalid format in savelogindata.txt: " + line);
                }
            }
            if (found) {
                // Get the user object for the logged-in user
                User user = users.get(loggedInUsername);
                if (user != null) {
                    // Add friend for the logged-in user
                    user.addFriend(friendUsername);
                    System.out.println("Friend added successfully.");

                    // Get the user object for the friend
                    User friend = users.get(friendUsername);
                    if (friend != null) {
                        // Add the logged-in user to the friend's friend list
                        friend.addFriend(loggedInUsername);
                    } else {
                        System.out.println("Friend user not found.");
                    }
                } else {
                    System.out.println("User not found.");
                }
            } else {
                System.out.println("Friend username not found.");
            }
        } catch (FileNotFoundException e) {
            System.out.println("savelogindata file not found.");
            e.printStackTrace();
        }
    }

    // Method to display the list of friends for the logged-in user
    public void showFriendsList() {
        if (loggedInUsername == null) {
            System.out.println("Please login first.");
            return;
        }
        // Get the user object for the logged-in user
        User user = users.get(loggedInUsername);
        if (user != null) {
            List<String> friends = user.getFriends();
            if (friends.isEmpty()) {
                System.out.println("You have no friends yet.");
            } else {
                System.out.println("Your friends:");
                for (String friend : friends) {
                    System.out.println(friend);
                }
            }
        } else {
            System.out.println("User not found.");
        }
    }

    // Method to display the list of friends for a given username
    public void showFriendsList(String username) {
        if (users.containsKey(username)) {
            User user = users.get(username);
            List<String> friends = user.getFriends();
            if (friends.isEmpty()) {
                System.out.println(username + " has no friends yet.");
            } else {
                System.out.println(username + "'s friends:");
                for (String friend : friends) {
                    System.out.println(friend);
                }
            }
        } else {
            System.out.println("User not found.");
        }
    }
    // Method to follow another user for the logged-in user
    public void followUser(String usernameToFollow) {
        User user = users.get(loggedInUsername);
        if (user != null && users.containsKey(usernameToFollow)) {
            user.followUser(usernameToFollow); // Follow another user
            System.out.println("You are now following " + usernameToFollow + ".");
        } else {
            System.out.println("User not found.");
        }
    }

    // Method to display the list of users followed by the logged-in user
    public void showFollowingList() {
        User user = users.get(loggedInUsername);
        if (user != null) {
            List<String> following = user.getFollowing();
            if (following.isEmpty()) {
                System.out.println("You are not following anyone yet.");
            } else {
                System.out.println("You are following:");
                for (String followedUser : following) {
                    System.out.println(followedUser);
                }
            }
        } else {
            System.out.println("User not found.");
        }
    }

    // Method to log out the user
    public void logout() {
        loggedInUsername = null; // Set logged-in user to null
        System.out.println("Logged out successfully.");
    }

    // Method to display a random post with comments and likes
    public void displayRandomPostWithCommentsAndLikes() {
        if (posts.isEmpty()) {
            System.out.println("No posts available.");
            return;
        }

        // Get a random post ID
        int randomPostId = getRandomPostId();

        // Get the random post
        Post randomPost = posts.get(randomPostId);

        // Display the random post
        if (randomPost != null) {
            System.out.println("Random Post:");
            System.out.println(randomPost);

            // Display comments
            System.out.println("Comments:");
            List<Comment> comments = randomPost.getComments();
            if (comments.isEmpty()) {
                System.out.println("No comments yet.");
            } else {
                for (Comment comment : comments) {
                    System.out.println(comment);
                }
            }

            // Display likes
            System.out.println("Likes: " + randomPost.getLikes().size());
        } else {
            System.out.println("Failed to retrieve random post.");
        }
    }

    // Method to get a random post ID
    private int getRandomPostId() {
        // Get a random index within the range of post IDs
        int randomIndex = ThreadLocalRandom.current().nextInt(1, Post.getNextPostId());
        return randomIndex;
    }

    // Main method to run the Facebook project
    public static void main(String[] args) {
        MyFaceBook1 app = new MyFaceBook1(); // Create an instance of the Facebook project
        Scanner scanner = app.scanner; // Get the scanner instance from the app

        boolean loggedIn = false;


      /*  // Login or register loop
        while (!loggedIn) {
            System.out.println("Choose an option:");
            System.out.println("1. Login");
            System.out.println("2. Register a new user");
            System.out.print("Enter option: ");
            int loginOption = Integer.parseInt(scanner.nextLine());

            switch (loginOption) {
                case 1:
                    if (app.login()) {
                        loggedIn = true;
                        System.out.println("Logged in successfully.");
                    } else {
                        System.out.println("Invalid username or password. Please try again.");
                    }
                    break;
                case 2:
                    app.registerUser();
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }

        boolean exit = false;

        // Main menu loop
        while (!exit) {
            System.out.println("Make a choice please:");
            System.out.println("1. Make a post");
            System.out.println("2. View Posts");
            System.out.println("3. View all Posts");
            System.out.println("4. Enter public info about you");
            System.out.println("5. Add friend");
            System.out.println("6. Show friends list");
            System.out.println("7. Follow someone");
            System.out.println("8. Show following list");
            System.out.println("9. Famous Trends");
            System.out.println("10. Logout");
            System.out.print("Enter option: ");
            int option = Integer.parseInt(scanner.nextLine());

            switch (option) {
                case 1:
                    app.createPost();
                    break;
                case 2:
                    app.viewPosts();
                    break;
                case 3:
                    app.viewAllPostsWithLikesAndComments();
                    break;
                case 4:
                    app.enterPublicInfo();
                    break;
                case 5:
                    app.addFriend();
                    break;
                case 6:
                    app.showFriendsList();
                    break;
                case 7:
                    System.out.print("Enter username to follow: ");
                    String userToFollow = scanner.nextLine();
                    app.followUser(userToFollow);
                    break;
                case 8:
                    app.showFollowingList();
                    break;
                case 9:
                    app.displayRandomPostWithCommentsAndLikes();
                    break;
                case 10:
                    app.logout();
                    loggedIn = false;
                    // Return to the login/register loop
                    while (!loggedIn) {
                        // Prompt user to choose login or registration again
                        System.out.println("Choose an option:");
                        System.out.println("1. Login");
                        System.out.println("2. Register a new user");
                        System.out.print("Enter option: ");
                        int loginOption;
                        try {
                            loginOption = Integer.parseInt(scanner.nextLine());
                        } catch (NumberFormatException e) {
                            // If user enters a non-integer option, notify user and repeat loop
                            System.out.println("Invalid option. Please enter a number.");
                            continue;
                        }

                        switch (loginOption) {
                            case 1:
                                // If user chooses login, attempt login

                                if (app.login()) {
                                    loggedIn = true; // Set logged-in flag to true
                                    System.out.println("Logged in successfully.");
                                } else {
                                    // If login fails, notify user and repeat loop
                                    System.out.println("Invalid username or password. Please try again.");
                                }
                                break;
                            case 2:
                                // If user chooses registration, register a new user
                                app.registerUser();
                                // After registration, attempt login
                                if (app.login()) {
                                    loggedIn = true; // Set logged-in flag to true
                                    System.out.println("Registered and logged in successfully.");
                                } else {
                                    // If login fails after registration, notify user and repeat loop
                                    System.out.println("Failed to login after registration. Please try again.");
                                }
                                break;
                            default:
                                // If user enters an invalid option, notify user and repeat loop
                                System.out.println("Invalid option. Please try again.");
                        }
                    }
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }

        scanner.close(); // Close the scanner
        */

    }


}
