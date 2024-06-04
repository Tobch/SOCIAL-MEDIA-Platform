/*
/*package com.example.demo8;


import javafx.application.Platform;
import javafx.stage.FileChooser;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.controlsfx.control.spreadsheet.Grid;
import java.io.*;
import java.util.*;

import static javafx.application.Application.launch;

public  class LoginWindow3 extends Application {

    private Stage primaryStage;
    Home_Page homePage = new Home_Page();
    public String loggedInUsername;
    public String loggedInUsernamepass;
    public String loggedInUsernameBIO;
    public String loggedInUsernamePUBLICINFO;
    public boolean loggedIn = false;
    public Scanner scanner; // Scanner for user input
    public Map<String, User> users = new HashMap<>(); // Map to store users by username
    public Map<Integer, Post> posts = new HashMap<>(); // Map to store posts by post ID

    User loggedinuser ;

    public void logout() {
        loggedInUsername = null;
        loggedinuser = null;
        // Set logged-in user to null
        System.out.println("Logged out successfully.");
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

    public void createPost(String content) {
        if (loggedInUsername == null) {
            System.out.println("Please login first.");
            return;
        }
        // Prompt for user input
        System.out.print("Enter post content: ");
        //String content = scanner.nextLine();

        // Create a new post object
        Post post = new Post(loggedInUsername, content);
        posts.put(post.getId(), post); // Add post to the map of posts
        System.out.println("Post created successfully.");
        savePostsToFile(); // Save posts to file after creating a new post
    }

    // Method to add a comment to a post
    public void addComment(int postId,String commentContent) {
        Post post = posts.get(postId);
        if (post != null) {
            // Prompt for user input
            System.out.print("Enter your comment: ");
            //String commentContent = scanner.nextLine();
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
        for (Post post : posts.values())
        {

            System.out.println(post); // Display the post
            System.out.println();
            // Check if user is logged in
            if (loggedInUsername != null) {
                // Prompt for user interaction
                System.out.print("Do you want to interact with this post? (comment/like/no): ");
                String interaction = scanner.nextLine();
                switch (interaction.toLowerCase()) {
                    case "comment":
                        //addComment(post.getId(),"");
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
    private void loadUserDataFromFile() {
        File file = new File("savelogindata.txt");
        if (!file.exists()) {
            System.out.println("User data file does not exist.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Assuming the format is "username,password,email,bio,profilePicturePath,publicInfo"
                String[] parts = line.split(",");
                if (parts.length == 6) {
                    String username = parts[0].trim();
                    String password = parts[1].trim();
                    String email = parts[2].trim();
                    String bio = parts[3].trim();
                    String profilePicturePath = parts[4].trim();
                    String publicInfo = parts[5].trim();

                    // Create a new User object with the parsed data
                    User user = new User(username, password, bio, profilePicturePath, publicInfo);
                           //User user1 = new
                    // Add the user to the map of users
                    users.put(username, user);

                } else {
                    System.out.println("Invalid user data format in file: " + line);
                }
            }
            System.out.println("User data loaded successfully.");
        } catch (FileNotFoundException e) {
            System.out.println("User data file not found.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("An error occurred while reading user data file.");
            e.printStackTrace();
        }
    }
    @Override
    public void start(Stage primaryStage)
    {

        loadUserDataFromFile();
        loggedinuser = users.get(loggedInUsername);

        this.primaryStage = primaryStage;
        primaryStage.setTitle("Login");

        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(25, 25, 25, 25));

        Button btnLogin = new Button("Login");
        Button btnNewUser = new Button("Login as a New User");

        btnLogin.setOnAction(e -> showLoginWindow());
        btnNewUser.setOnAction(e -> showNewUserWindow());

        vbox.getChildren().addAll(btnLogin, btnNewUser);

        Scene scene = new Scene(vbox, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showLoginWindow() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Label lblUsername = new Label("Username:");
        TextField txtUsername = new TextField();
        Label lblPassword = new Label("Password:");
        PasswordField txtPassword = new PasswordField();
        Button btnOk = new Button("OK");

        btnOk.setOnAction(e -> {
            try {
                if (checkCredentials(txtUsername.getText(), txtPassword.getText())) {
                    System.out.println("Login successful!");
                    loggedInUsername = txtUsername.getText();
                    savelogindata( txtUsername.getText(),txtPassword.getText());

                    //loggedInUsernamepass=txtPassword.getText();
                    //loggedinuser = users.get(loggedInUsername);

                    //homePage.start(primaryStage);
                    HomePageWindow();


                } else {
                    System.out.println("Login failed!");
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        grid.add(lblUsername, 0, 0);
        grid.add(txtUsername, 1, 0);
        grid.add(lblPassword, 0, 1);
        grid.add(txtPassword, 1, 1);
        grid.add(btnOk, 1, 2);

        Scene loginScene = new Scene(grid, 300, 200);
        primaryStage.setScene(loginScene);

    }


    private boolean checkCredentials(String username, String password) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("savelogindata.txt"));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length == 6) {
                if (parts[0].equals(username) && parts[1].equals(password)) {
                    reader.close();
                    return true;
                }
            }
        }
        reader.close();
        return false;
    }


    private void showNewUserWindow()
    {


        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        Label lblUsername = new Label("Username:");
        TextField txtUsername = new TextField();

        Label lblPassword = new Label("Password:");
        TextField txtPassword = new TextField();

        // ... other components
        Label lblEmail = new Label("Email:");
        TextField txtEmail = new TextField();
        Label lblBio = new Label("Bio:");
        TextField txtBio = new TextField();
        //this.loggedInUsernameBIO = txtBio.getText();           /////////////////////

        Label lblPublicInfo = new Label("Public Information:");
        TextField txtPublicInfo = new TextField();

        //loggedInUsernamePUBLICINFO = txtPublicInfo.getText();

        // Components for profile picture upload
        Label lblProfilePicture = new Label("Profile Picture:");
        TextField txtProfilePicture = new TextField();
        txtProfilePicture.setEditable(false); // Prevent manual editing
        Button btnUploadPicture = new Button("Upload Picture");

        btnUploadPicture.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Profile Picture");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", ".png", ".jpg", "*.jpeg")
            );
            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            if (selectedFile != null) {
                txtProfilePicture.setText(selectedFile.getAbsolutePath());
            }
        });

        Button btnOk = new Button("OK");
        btnOk.setOnAction(e -> {
            try {
                registerNewUser(

                        txtUsername.getText(),
                        txtPassword.getText(),
                        txtEmail.getText(),
                        txtBio.getText(),
                        txtProfilePicture.getText(),
                        txtPublicInfo.getText()
                );
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        // Add components to the grid
        grid.add(lblUsername, 0, 0);
        grid.add(txtUsername, 1, 0);
        grid.add(lblPassword, 0, 1);
        grid.add(txtPassword, 1, 1);
        grid.add(lblEmail, 0, 2);
        grid.add(txtEmail, 1, 2);
        grid.add(lblBio, 0, 3);
        grid.add(txtBio, 1, 3);
        grid.add(lblPublicInfo, 0, 4);
        grid.add(txtPublicInfo, 1, 4);
        grid.add(lblProfilePicture, 0, 5);
        grid.add(txtProfilePicture, 1, 5);
        grid.add(btnUploadPicture, 2, 5);
        grid.add(btnOk, 1, 6);

        Scene newUserScene = new Scene(grid, 400, 400);
        primaryStage.setScene(newUserScene);
    }


    private void registerNewUser(String username, String password, String email, String bio, String profilePicturePath, String publicInfo) throws IOException {
        File file = new File("savelogindata.txt");
        if (!file.exists()) {
            file.createNewFile(); // Create the file if it does not exist
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            // Write all user information to the file in a comma-separated format
            writer.write(username + "," + password + "," + email + "," + bio + "," + profilePicturePath + "," + publicInfo);
            writer.newLine();
            User registeredUser = new User(username,password,bio,email,profilePicturePath);

            //users.put(username,registeredUser);

            System.out.println("New user registered successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred while saving user data to file.");
            e.printStackTrace();
        }
    }




    public static void main(String[] args)
    {
        launch();


    }

    private void HomePageWindow() {

        primaryStage.setTitle("Simple Social Media App");

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));
        root.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("Simple Social Media App");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Label choiceLabel = new Label("Make a choice please:");

        Button makePostButton = new Button("Make a post");
        Button viewPostsButton = new Button("View Posts");
        Button viewAllPostsButton = new Button("View all Posts");
        Button editifButton = new Button("Edit info");
        Button addFriendButton = new Button("Add friend");
        Button showFriendsListButton = new Button("Show friends list");
        Button followButton = new Button("Follow someone");
        Button showFollowingListButton = new Button("Show following list");
        Button famousTrendsButton = new Button("Famous Trends");
        Button logoutButton = new Button("Logout");

        //makePostButton.setOnAction(e -> createPost());
        makePostButton.setOnAction(e -> {
            // MakePostWindow makePostWindow = new MakePostWindow();
            // makePostWindow.show();
            MakePostWindow();

        });
        editifButton.setOnAction(e -> {
            showUserDetailsWindow();





        });

        viewPostsButton.setOnAction(e -> {
            showPostsWindow();
            //ViewPostsWindow viewPostsWindow = new ViewPostsWindow(app);
            // viewPostsWindow.show();
        });


        /*viewAllPostsButton.setOnAction(e -> viewAllPostsWithLikesAndComments());
        enterPublicInfoButton.setOnAction(e -> enterPublicInfo());
        addFriendButton.setOnAction(e -> addFriend());
        showFriendsListButton.setOnAction(e -> showFriendsList());
        TextField txf1 = new TextField();
        followButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                followUser(txf1.getText());
            }
        });
        showFollowingListButton.setOnAction(e -> showFollowingList());
        famousTrendsButton.setOnAction(e -> displayRandomPostWithCommentsAndLikes());
                           */
/*

        logoutButton.setOnAction(e -> {
            logout();
            //primaryStage.close(); // back to login page
            start(primaryStage);
        });

        root.getChildren().addAll(
                titleLabel, choiceLabel,
                makePostButton, viewPostsButton, viewAllPostsButton,
                editifButton , addFriendButton, showFriendsListButton,
                followButton, showFollowingListButton, famousTrendsButton, logoutButton
        );

        Scene scene = new Scene(root, 400, 500);
        primaryStage.setScene(scene);
    }

    private void MakePostWindow() {
        GridPane pane = new GridPane();

        Label title = new Label("Enter Post");
        TextField postTxt = new TextField();

        Button btPost = new Button("CREATE");
        //pane.getChildren().add(title,0,0);
        Label text = new Label();


        pane.getChildren().add(text);
        pane.getChildren().add(title);
        pane.getChildren().add(postTxt);
        pane.getChildren().add(btPost);
        btPost.setOnAction(e -> {
                    text.setText("Post added successfuly");
                    HomePageWindow();
                    createPost(postTxt.getText());
                }

        );
        Scene scene = new Scene(pane, 200, 400);
        primaryStage.setScene(scene);
        primaryStage.show();

    }





    public void showPostsWindow() {

        loadPostsFromFile();
        Platform.runLater(() -> {
            Stage primaryStage = new Stage();
            primaryStage.setTitle("View Posts");

            VBox root = new VBox(10);
            root.setAlignment(Pos.CENTER);
            root.setPadding(new Insets(10));

            // ListView to display posts
            ListView<VBox> postsListView = new ListView<>();
            postsListView.setPrefHeight(400);

            // Iterate over the posts and add them to the ListView
            for (Post post : posts.values())
            {

                Label postLabel = new Label(post.toString());
                Button commentButton = new Button("Comment");
                Button viewCommentButton = new Button("View Comments");
                Button likeButton = new Button("Like");
                TextField commentTxt = new TextField();
                // Set up event handlers for the buttons
                commentButton.setOnAction(e -> {
                    // Prompt for comment and add it to the post
                    addComment(post.getId(),commentTxt.getText());

                    //addComment(post.getId(),);

                });
                likeButton.setOnAction(e -> {

                    likePost(post.getId());
                    System.out.println("You liked the post.");
                    // Update the UI to reflect the new like count

                    likeButton.setText("Like (" + post.getLikes().size() + ")");


                });


                VBox postBox = new VBox(postLabel, commentButton,commentTxt, likeButton);
                postBox.setSpacing(5);
                postsListView.getItems().add(postBox);
            }

            root.getChildren().add(postsListView);

            Scene scene = new Scene(root, 400, 500);
            primaryStage.setScene(scene);
            primaryStage.show();
        });
    }




    private void addCommentWindow() {
        GridPane pane = new GridPane();
        Label title = new Label("Enter Comment");
        TextField commentTxt = new TextField();

        Button btPost = new Button("CREATE");
        //pane.getChildren().add(title,0,0);
        Label text = new Label();


        pane.getChildren().add(text);
        pane.getChildren().add(title);
        pane.getChildren().add(commentTxt);
        pane.getChildren().add(btPost);

        btPost.setOnAction(e -> {
                    text.setText("Post added successfuly");
                    HomePageWindow();
                    // addComment(post.getId(),commentTxt.getText());
                }

        );
        Scene scene = new Scene(pane, 200, 400);
        primaryStage.setScene(scene);
        primaryStage.show();

    }


    public void showUserDetailsWindow()
    {

        Stage userDetailsStage = new Stage();
        userDetailsStage.setTitle("User Details");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));

        // Fetch user details from the 'users' map using loggedInUsername
        // User loggedInUser = users.get(loggedInUsername); // Replace with your actual logged in user variable

        // Create labels and text fields with user information
        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField(loggedinuser.getUsername());
        nameField.setEditable(false); // Initially not editable
        Button editNameButton = new Button("Edit");
        setupEditButton(nameField, editNameButton); // Setup the edit button

        Label passwordLabel = new Label("Password:");
        TextField passwordField = new TextField(loggedinuser.getPassword());
        passwordField.setEditable(false); // Initially not editable
        Button editPasswordButton = new Button("Edit");
        setupEditButton(passwordField, editPasswordButton); // Setup the edit button

        Label bioLabel = new Label("Bio:");
        TextField bioArea = new TextField(loggedinuser.getBio());
        bioArea.setEditable(false); // Initially not editable
        Button editBioButton = new Button("Edit");
        setupEditButton(bioArea, editBioButton); // Setup the edit button

        Label publicInfoLabel = new Label("Public Info:");
        TextField publicInfoArea = new TextField(loggedinuser.getPublicInfo());
        publicInfoArea.setEditable(false); // Initially not editable
        Button editPublicInfoButton = new Button("Edit");
        setupEditButton(publicInfoArea, editPublicInfoButton); // Setup the edit button

        // Add components to the grid
        grid.add(nameLabel, 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(editNameButton, 2, 0);

        grid.add(passwordLabel, 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(editPasswordButton, 2, 1);

        grid.add(bioLabel, 0, 2);
        grid.add(bioArea, 1, 2);
        grid.add(editBioButton, 2, 2);

        grid.add(publicInfoLabel, 0, 3);
        grid.add(publicInfoArea, 1, 3);
        grid.add(editPublicInfoButton, 2, 3);

        // Set the scene and show the stage
        Scene scene = new Scene(grid, 400, 300);
        userDetailsStage.setScene(scene);
        userDetailsStage.show();
    }

    private void setupEditButton(TextField field, Button button) {
        button.setOnAction(e -> {
            if (!field.isEditable()) {
                field.setEditable(true);
                field.requestFocus(); // Set focus to the editable field
            } else {
                field.setEditable(false); // Make field non-editable after editing is done
                // Here you can also implement saving the edited data
            }
        });
    }







}

*/
