package com.example.demo8;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Home_Page extends Application {


    @Override
    public void start(Stage primaryStage) {
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
        Button enterPublicInfoButton = new Button("Enter public info about you");
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
        });
        viewPostsButton.setOnAction(e -> {
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
        logoutButton.setOnAction(e -> {
            //logout();
            primaryStage.close(); // back to login page
        });

        root.getChildren().addAll(
                titleLabel, choiceLabel,
                makePostButton, viewPostsButton, viewAllPostsButton,
                enterPublicInfoButton, addFriendButton, showFriendsListButton,
                followButton, showFollowingListButton, famousTrendsButton, logoutButton
        );

        Scene scene = new Scene(root, 400, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}