package controller.administator;

import applikacja.Dialogs;
import applikacja.SceneManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import models.dao.UsersDAO;
import models.entity.Users;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class AdminController {

    public Text text;
    public TableView<Users> tabela;
    public TableColumn<Users,String> imie;
    public TableColumn<Users,String> nazwisko;
    public TableColumn<Users,String> login;

    String rola="HALL";

    ObservableList<Users> getProductList(String rola){

        List<Users> users = new UsersDAO().findUserByRole(rola);
        ObservableList<Users> accounts = FXCollections.observableArrayList();

        users.forEach(user->{
            accounts.add(user);
        });

    	return accounts;

    }
    
    public void initialize() {

        imie.setCellValueFactory(new PropertyValueFactory<>("firstName"));
  	    nazwisko.setCellValueFactory(new PropertyValueFactory<>("lastName"));
  	    login.setCellValueFactory(new PropertyValueFactory<>("login"));

  	    tabela.setItems(getProductList(rola));
        
    }
    
    public void changeTabSala()
    {

    	 text.setText("Sala");
    	 rola="HALL";

    	 tabela.setItems(getProductList(rola));

    }
    
    public void changeTabKuchnia()
    {

        text.setText("Kuchnia");
        rola="KITCHEN";

        tabela.setItems(getProductList(rola));

    }
    
    public void changeTabKsieg()
    {

        text.setText("Księgowość");
        rola="BOOKKEEPING";
        tabela.setItems(getProductList(rola));

    }
    
    public void addUser() throws IOException {

        addUserController.rola=rola;
        SceneManager.renderScene("addUser");

    }

    public void deleteUser(){

        Users person = tabela.getSelectionModel().getSelectedItem();
        new UsersDAO().delete(person);

        tabela.setItems(getProductList(rola));

    }


    public void back() {

        Optional<ButtonType> result = Dialogs.confirmLogOutDialog();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            SceneManager.renderScene("login");
        }

    }
    
}
