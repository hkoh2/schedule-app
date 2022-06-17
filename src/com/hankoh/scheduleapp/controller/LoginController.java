package com.hankoh.scheduleapp.controller;

import com.hankoh.scheduleapp.DAO.UserDao;
import com.hankoh.scheduleapp.model.DataStorage;
import com.hankoh.scheduleapp.model.User;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Login controller. Validates user credentials for login.
 *
 * Display language is set based on display language and local.
 *
 * Lambda - Instead of attaching an event handler to the button, a lambda
 * function was used to terminate the application.
 */
public class LoginController {
    public Text titleText;
    public Button exitButton;
    public Button loginButton;
    public Text loginErrorText;
    public Label passwordLabel;
    public Label usernameLabel;
    public ComboBox<String> languageCombo;
    public Button devLoginButton;
    private ObservableList<String> language = FXCollections.observableArrayList("English", "Français");
    public TextField password;
    public TextField username;

    public void initialize() {

        // Resource Bundle for i18n
        ResourceBundle msg = ResourceBundle.getBundle("com.hankoh.scheduleapp.properties.MessagesBundle", Locale.getDefault());
        titleText.setText(msg.getString("login.title"));
        exitButton.setText(msg.getString("login.exit_button"));
        loginButton.setText(msg.getString("login.login_button"));
        usernameLabel.setText(msg.getString("login.username"));
        passwordLabel.setText(msg.getString("login.password"));

        languageCombo.getItems().addAll("english", "français");
        String currentLanguage = Locale.getDefault().getDisplayLanguage();
        languageCombo.getSelectionModel().select(currentLanguage);

        // lambda for closing app
        exitButton.setOnAction(event -> Platform.exit());

    }

    public void onExitButtonClick() {
        // not used.

    }

    public void onLoginButtonClick(ActionEvent actionEvent) throws SQLException, IOException {
        UserDao userDao = new UserDao();

        // Checks username and password.
        // Also adds user to DS.
        if (userDao.loginUser(username.getText(), password.getText())) {
            System.out.println("Logged in");
            loginErrorText.setText("Logged In!");
            Logger.loginAttempted(username.getText(), true);
            loginUser(actionEvent);
        } else {
            System.out.println("Wrong username or password");
            loginErrorText.setText("Wrong username or password");
            Logger.loginAttempted(username.getText(), false);
            // provide error messages.
        }
    }

    private void loginUser(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/hankoh/scheduleapp/view/main.fxml")));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Schedule App");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void onLanguageSelect(ActionEvent actionEvent) {
        String langVal = languageCombo.getValue();
        switch (langVal) {
            case "english":
                System.out.println("English selected");
                break;
            case "français":
                System.out.println("French selected");
                break;
            default:
                break;
        }
    }

    public void onDevLoginClick(ActionEvent actionEvent) throws IOException {
        DataStorage.getInstance().setUser(new User(9999, "dev"));
        loginUser(actionEvent);
    }
}
