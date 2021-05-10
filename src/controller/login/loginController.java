package controller.login;

import applikacja.Main;
import applikacja.SceneManager;
import at.favre.lib.crypto.bcrypt.BCrypt;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import models.dao.UsersDAO;
import models.entity.Users;

import java.util.List;

public class loginController {
    @FXML
    public PasswordField passwordText;
    @FXML
    public TextField loginText;
    @FXML
    public Label badLoginOrPasswordLabel;

    @FXML
    void initialize() {
        badLoginOrPasswordLabel.setVisible(false);
    }


    public void loginOnAction(ActionEvent actionEvent) {

        List<Users> usersList = new UsersDAO().findAll();

        for (Users users : usersList) {
            if (users.getLogin().equals(this.loginText.getText())) {
                BCrypt.Result result = BCrypt.verifyer().verify(this.passwordText.getText().toCharArray(), users.getPassword());

                if (result.verified)
                    userSuccessfullyLoaded(users);
                break;

            }
        }
        badLoginOrPasswordLabel.setVisible(true);

    }

    private void userSuccessfullyLoaded(Users user) {

        switch (user.getRole()) {
            case "CLIENT" -> {
                SceneManager.renderScene("client");
                Main.logged="client";
                Main.loggedID = user.getUsers_id();
            }
            case "HALL" -> {
                SceneManager.renderScene("hall");
                Main.logged="kelner";
                Main.loggedID = user.getUsers_id();
            }
            case "KITCHEN" -> {
                SceneManager.renderScene("kitchen");
                Main.logged="kitchen";
                Main.loggedID = user.getUsers_id();
            }
            case "BOOKKEEPING" -> {
                SceneManager.renderScene("bookkeeping");
                Main.logged="bookkeeping";
                Main.loggedID = user.getUsers_id();
            }
            case "ADMIN" -> {
                SceneManager.renderScene("admin");
                Main.logged="admin";
                Main.loggedID = user.getUsers_id();
            }
        }

    }

    public void setOnKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            loginOnAction(null);
        }
    }

    public void clientPanelOnAction(ActionEvent actionEvent) {
        SceneManager.renderScene("client");
    }
}
