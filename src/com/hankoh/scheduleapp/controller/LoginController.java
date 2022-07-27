package com.hankoh.scheduleapp.controller;

import com.hankoh.scheduleapp.DAO.UserDao;
import com.hankoh.scheduleapp.model.DataStorage;
import com.hankoh.scheduleapp.model.User;
import javafx.application.Platform;
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
 * <p>
 * Display language is set based on display language and local.
 * <p>
 * Lambda - Instead of attaching an event handler to the button, a lambda
 * function was used to terminate the application.
 */
public class LoginController {
    /**
     * The Title text.
     */
    public Text titleText;
    /**
     * The Exit button.
     */
    public Button exitButton;
    /**
     * The Login button.
     */
    public Button loginButton;
    /**
     * The Login error text.
     */
    public Text loginErrorText;
    /**
     * The Password label.
     */
    public Label passwordLabel;
    /**
     * The Username label.
     */
    public Label usernameLabel;
    /**
     * The Language combo.
     */
    public ComboBox<String> languageCombo;
    /**
     * The Dev login button.
     */
    public Button devLoginButton;
    /**
     * The Password.
     */
    public TextField password;
    /**
     * The Username.
     */
    public TextField username;

    /**
     * Initialize.
     */
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

    /**
     * On login button click.
     *
     * @param actionEvent the action event
     * @throws SQLException the sql exception
     * @throws IOException  the io exception
     */
    public void onLoginButtonClick(ActionEvent actionEvent) throws SQLException, IOException {
        UserDao userDao = new UserDao();

        // Checks username and password.
        // Also adds user to DS.
        if (userDao.loginUser(username.getText(), password.getText())) {
            loginErrorText.setText("Logged In!");
            Logger.loginAttempted(username.getText(), true);
            loginUser(actionEvent);
        } else {
            loginErrorText.setText("Wrong username or password");
            Logger.loginAttempted(username.getText(), false);
        }
    }

    /**
     * Logs user in to main screen.
     *
     * @param actionEvent
     * @throws IOException
     */
    private void loginUser(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/hankoh/scheduleapp/view/main.fxml")));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Schedule App");
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * On language select.
     *
     * @param actionEvent the action event
     */
    public void onLanguageSelect(ActionEvent actionEvent) {
        String langVal = languageCombo.getValue();
        switch (langVal) {
            case "english" -> System.out.println("English selected");
            case "français" -> System.out.println("French selected");
            default -> {
            }
        }
    }

    /**
     * On dev login click.
     *
     * @param actionEvent the action event
     * @throws IOException the io exception
     */
    public void onDevLoginClick(ActionEvent actionEvent) throws IOException {
        DataStorage.getInstance().setUser(new User(9999, "dev"));
        loginUser(actionEvent);
    }
}
