package controller.administator;
import applikacja.SceneManager;
import at.favre.lib.crypto.bcrypt.BCrypt;
import javafx.event.ActionEvent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import models.dao.UsersDAO;
import models.entity.Orders;
import models.entity.Users;


import java.util.ArrayList;

public class addUserController {

    public TextField lastname;
    public TextField login;
    public TextField firstname;
    public PasswordField password;
    public static String rola;
    public Text firstName_validation;
    public Text lastName_validation;
    public Text login_validation;
    public Text password_validation;

    public void back(ActionEvent actionEvent) {

        SceneManager.renderScene("admin");

    }

    public void Add(ActionEvent actionEvent) {
        Boolean state = true;
        ArrayList<Orders> orderList = new ArrayList<>();
        String firstName = firstname.getText();
        String lastName = lastname.getText();
        String Login = login.getText();
        String Password = password.getText();

        if(firstName.isEmpty()){
            firstName_validation.setVisible(true);
            state = false;
        }else{
            firstName_validation.setVisible(false);
        }

        if(lastName.isEmpty()){
            lastName_validation.setVisible(true);
            state = false;
        }else{
            lastName_validation.setVisible(false);
        }

        if(!Login.isEmpty()){
            if(Login.length()<6){
                login_validation.setText("Login musi mieć co najmniej 6 znaków.");
                login_validation.setVisible(true);
                state = false;
            }else if(!new UsersDAO().findUserByLogin(Login)){
                login_validation.setText("Login zajęty.");
                login_validation.setVisible(true);
                state = false;
            }else{
                login_validation.setVisible(false);
            }
        }else{
            login_validation.setVisible(true);
            state = false;
        }

        if(!Password.isEmpty()){
            if(Password.length()<6){
                password_validation.setText("Hasło musi mieć co najmniej 6 znaków");
                password_validation.setVisible(true);
                state = false;
            }else{
                password_validation.setVisible(false);
            }
        }else{
            password_validation.setVisible(true);
            state = false;
        }

        if(state) {
            String bCryptedPasswd = BCrypt.withDefaults().hashToString(12, password.getText().toCharArray());

            Users user = new Users(firstName,lastName,Login,bCryptedPasswd,rola,orderList);

            new UsersDAO().save(user);

            SceneManager.renderScene("admin");
        }
    }
}
